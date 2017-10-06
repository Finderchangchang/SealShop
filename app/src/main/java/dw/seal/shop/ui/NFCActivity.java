package dw.seal.shop.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;

import net.tsz.afinal.view.TitleBar;

import org.jetbrains.annotations.NotNull;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dw.seal.shop.BaseActivity;
import dw.seal.shop.R;
import dw.seal.shop.callback.JsonCallback;
import dw.seal.shop.callback.LzyResponse;
import dw.seal.shop.http.ReadSoapObject;
import dw.seal.shop.http.WebServiceUtils;
import dw.seal.shop.model.SignetModel;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/2.
 */

public class NFCActivity extends BaseActivity {
    private TextView txt_title_bar;
    private byte[] DFN_SRVa;//A区密码
    private byte[] DFN_SRVb;//B区密码
    PendingIntent mPendingIntent;
    NfcAdapter nfcAdapter;
    private int byteLength = 0;
    private int byteindex = 0;
    byte[] MyResult;
    byte[] MyTitleResult;
    private int titleIndex = 0;
    private Dialog myProgressDialog;//进度条
    private ImageView imgBack;//返回
    TextView title_tv;
    TextView faren_tv;
    TextView sp_state_tv;
    TextView time_tv;

    TextView title_tv1;
    TextView faren_tv1;
    TextView sp_state_tv1;
    TextView time_tv1;
    LinearLayout top_ll;
    LinearLayout top_ll1;
    boolean key = false;//NFC功能（本页进行比对操作）
    TitleBar titleBar;
    RelativeLayout iv;
    Button hy_btn;
    boolean is_start;

    private void loadEvent() {
        is_start = getIntent().getBooleanExtra("is_start", false);

        hy_btn = (Button) findViewById(R.id.hy_btn);
        hy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("model", siModel);
                setResult(66, intent);
                finish();
            }
        });
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        iv = (RelativeLayout) findViewById(R.id.iv);
        titleBar.setLeftClick(new TitleBar.LeftClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
        if (is_start) {
            titleBar.setCenter_str("NFC印章识别");
            titleBar.set_no_left_iv(true);
            hy_btn.setVisibility(View.GONE);
        }
        title_tv = (TextView) findViewById(R.id.title_tv);
        faren_tv = (TextView) findViewById(R.id.faren_tv);
        sp_state_tv = (TextView) findViewById(R.id.sp_state_tv);
        time_tv = (TextView) findViewById(R.id.time_tv);

        title_tv1 = (TextView) findViewById(R.id.title_tv1);
        faren_tv1 = (TextView) findViewById(R.id.faren_tv1);
        sp_state_tv1 = (TextView) findViewById(R.id.xq_tv1);
        time_tv1 = (TextView) findViewById(R.id.time_tv1);
        top_ll = (LinearLayout) findViewById(R.id.top_ll);
        top_ll1 = (LinearLayout) findViewById(R.id.top1_ll);
        key = getIntent().getBooleanExtra("key", true);//默认是NFC识别
        readCard();
    }

    private void loadView() {
        DFN_SRVa = new byte[]{(byte) getStringNum("DE"), (byte) getStringNum("39"), (byte) getStringNum("A5"),
                (byte) getStringNum("CF"), (byte) getStringNum("03"), (byte) getStringNum("17")};
        DFN_SRVb = new byte[]{(byte) getStringNum("14"), (byte) getStringNum("27"), (byte) getStringNum("FA"),
                (byte) getStringNum("CC"), (byte) getStringNum("0E"), (byte) getStringNum("BC")};
    }

    //读取芯片
    private void readCard() {
        Intent nfcIntent = new Intent(NFCActivity.this, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent =
                PendingIntent.getActivity(NFCActivity.this, 0, nfcIntent, 0);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(getApplicationContext(), "NFC feature is supported on this device.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    private void processIntent(Intent intent) {
        byteindex = 0;
        titleIndex = 0;
        String metaInfo = "";
        //取出封装在intent中的TAG
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        byte[] tagId = tagFromIntent.getId();
        String str = ByteArrayToHexString(tagId);
        str = flipHexStr(str);
        Long cardNo = Long.parseLong(str, 16);
        metaInfo = "卡号：" + cardNo.toString();
        //tvContext.setText(cardNo.toString());

        boolean auth = false;
        //读取TAG
        MifareClassic mfc = MifareClassic.get(tagFromIntent);
        try {
            //Enable I/O operations to the tag from this TagTechnology object.
            mfc.connect();
            //processIntentgetNum(mfc);
            MyResult = new byte[192];
            MyTitleResult = new byte[32];
            int type = mfc.getType();//获取TAG的类型
            int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数
            Boolean isTrue = true;
            for (int j = 0; j < sectorCount; j++) {
                //Authenticate a sector with key A.
                if (j % 2 == 0) {
                    auth = mfc.authenticateSectorWithKeyB(j,
                            DFN_SRVb);
                } else {
                    auth = mfc.authenticateSectorWithKeyA(j,
                            DFN_SRVa);
                }
                int bCount;
                int bIndex;
                if (auth) {
                    metaInfo += "Sector " + j + ":验证成功\n";
                    // 读取扇区中的块
                    bCount = mfc.getBlockCountInSector(j);
                    bIndex = mfc.sectorToBlock(j);
                    for (int i = 0; i < bCount; i++) {
                        byte[] data = mfc.readBlock(bIndex);
//                        metaInfo += "Block " + bIndex + " : "
//                                + bytesToHexString(data) + "\n";
                        //String str16 = bytesToHexString(data);
                        //System.out.println(j+":"+i+":"+str16 + "-mymessage:" + Base64.encodeToString(data, Base64.DEFAULT));
                        if (j == 0 && i < 3 && i > 0) {
                            addTitleByte(data);
                            System.out.println("0扇区：" + Base64.encodeToString(MyTitleResult, Base64.DEFAULT));
                            //getMessage(Base64.encodeToString(MyTitleResult, Base64.DEFAULT), "ASCII");
                            if (key) {

                            }
                        } else if (j > 0 && j < 5) {
                            if (i < 3) {
                                getAddByte(data);
                            }
                            if (j == 4 && i == 2) {
                                System.out.println("11-------********mymessage:" + Base64.encodeToString(MyResult, Base64.DEFAULT));
                                if (isTrue) {
                                    String kk = Base64.encodeToString(MyResult, Base64.DEFAULT);
                                    getMessage(Base64.encodeToString(MyResult, Base64.DEFAULT), "Unicode");
                                    getSignetId("1306001000001");

                                } else {
                                    Toast.makeText(NFCActivity.this, "链接丢失,请重新放卡", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
//                        else if (j == 4) {
//                            if (i < 1) {
//                                getAddByte(data);
//
//                            }
//                        }
                        bIndex++;
                    }
                } else {
                    isTrue = false;
                    metaInfo += "Sector " + j + ":验证失败\n";
                }
            }
            System.out.println("metainfoxxxx----" + metaInfo);
            //tvContext.setText(metaInfo);
        } catch (Exception e) {
            Toast.makeText(NFCActivity.this, "链接丢失", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void getSignetId(String id) {

        List<SignetModel> list = new ArrayList();
        OkGo.post("http://www.hbyzcx.com/Views/Handler/GetCorpSignets.ashx?SignetId=" + id)//
                .execute(new JsonCallback<LzyResponse<SignetModel>>() {
                    @Override
                    public void onSuccess(LzyResponse<SignetModel> model, Call call, Response response) {
                        SignetModel s = model.getSignet().get(0);
                        if (model != null) {
                            title_tv1.setText(s.getSignetContent());
                            faren_tv1.setText(s.getSignetSpecification());
                            sp_state_tv1.setText(s.getSignetStatusName());
                            //time_tv1.setText(model.getSignetCreateTime());
                            time_tv1.setText("2012-06-18");
                            read_success = true;
                            top_ll.setVisibility(View.VISIBLE);
                            top_ll1.setVisibility(View.GONE);
                        } else {
                            /*if (!read_success) {
                                top_ll1.setVisibility(View.VISIBLE);
                                top_ll.setVisibility(View.GONE);
                            }*/
                        }
                    }
                });

    }

    private void getAddByte(byte[] add) {
        for (int i = 0; i < add.length; i++) {
            MyResult[byteindex] = add[i];
            byteindex = byteindex + 1;
        }
        System.out.println("mymessage---" + byteindex);
    }

    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F"};
        String out = "";
        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    SignetModel siModel = new SignetModel();

    private void addTitleByte(byte[] add) {
        for (int i = 0; i < add.length; i++) {
            MyTitleResult[titleIndex] = add[i];
            titleIndex = titleIndex + 1;
        }

        System.out.println("mymessage1111---" + titleIndex);
    }

    //将c#byte转为Java数组
    private int getStringNum(String str) {
        int num = Integer.parseInt(str, 16);
        if (num >= 128) {
            num = num - 256;
        }
        return num;
    }

    @NonNull
    private String flipHexStr(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i <= s.length() - 2; i = i + 2) {
            result.append(new StringBuilder(s.substring(i, i + 2)).reverse());
        }
        return result.reverse().toString();
    }


    private void getMessage(String val, String type) {
        try {
            switch (type) {
                case "ASCII": {
                    HashMap<String, String> properties = new HashMap<String, String>();
                    properties.put("encryptString", val);//必填
                    properties.put("type", type);//必填
                    WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL, "DecrytionSignet", properties, new WebServiceUtils.WebServiceCallBack() {
                        @Override
                        public void callBack(SoapObject result) {
                            // myProgressDialog.dismiss();
                            if (result != null) {
                                String re = ReadSoapObject.getNewPassWord(result, "DecrytionSignet");
                                System.out.println("re:" + re);
                                String[] str = re.split("\\|");
                            } else {
                                Toast.makeText(NFCActivity.this, "加载失败！点击刷新！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
                case "Unicode": {
                    HashMap<String, String> properties = new HashMap<String, String>();
                    properties.put("encryptString", val);//必填
                    properties.put("type", type);//必填
                    WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL, "DecrytionSignet", properties, new WebServiceUtils.WebServiceCallBack() {
                        @Override
                        public void callBack(SoapObject result) {
                            // myProgressDialog.dismiss();
                            if (result != null) {
                                String re = ReadSoapObject.getNewPassWord(result, "DecrytionSignet");
                                System.out.println("re:" + re);
                                String[] str = re.split("\\|");
                                SignetModel signetModel = new SignetModel();
                                signetModel.setSignetTypeName(str[0]);
                                signetModel.setSignetMaterialName(str[1]);
                                signetModel.setSignetContent(str[2]);
                                signetModel.setCorpAddress(str[3]);
                                signetModel.setCorpLinkWay(str[4]);
                                signetModel.setCorpLinker(str[5]);
                                signetModel.setCorpCreateDate(str[6]);
                                signetModel.setCorpName(str[7]);
                                signetModel.setRegApprover(str[8]);
                                signetModel.setCorpId(str[9]);
                                signetModel.setSignetId("1306001000001");
                                siModel = signetModel;
                                //if (key) {
                                title_tv.setText(signetModel.getSignetContent());
                                faren_tv.setText(signetModel.getSignetMaterialName());
                                //sp_state_tv.setText("已交付");
                                time_tv.setText(signetModel.getCorpCreateDate());
                                top_ll.setVisibility(View.VISIBLE);
                                //top_ll1.setVisibility(View.VISIBLE);
                                iv.setVisibility(View.VISIBLE);
                                //} else {
//
//                                }
                            } else {
                                Toast.makeText(NFCActivity.this, "加载失败！点击刷新！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

        } catch (Exception e) {
            Toast.makeText(NFCActivity.this, "网络连接异常！", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //如果本程序打开，先调用这个窗体
        nfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //
        //if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
        processIntent(intent);
        //}
    }

    boolean read_success = false;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_nfc);
        nfcAdapter = NfcAdapter.getDefaultAdapter(NFCActivity.this);
        if (nfcAdapter == null) {
            Toast.makeText(NFCActivity.this, "设备不支持NFC!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (nfcAdapter.isEnabled()) {
                //if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
                //    readFromTag(getIntent());
                //}
            } else {
                Toast.makeText(NFCActivity.this, "请在系统设置中先启动NFC功能！", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        loadView();
        loadEvent();
    }

    @Override
    public void initEvents() {

    }
}
