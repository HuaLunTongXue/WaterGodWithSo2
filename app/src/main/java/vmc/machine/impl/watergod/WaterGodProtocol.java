package vmc.machine.impl.watergod;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

public class WaterGodProtocol extends AppCompatActivity {

    private static final String TAG = WaterGodProtocol.class.getSimpleName();
    private Thread startThread;
    private Thread getIdThread;
    private boolean isBreak = true;

    static {
        System.loadLibrary("WaterDispenser");
    }

    private EditText et;
    private EditText et2;
    private TextView tv;
    private String appendHead = "从VMC接收的消息：";
    private String liquid;
    private String mMachineId;
    private int count = 0;
    private Handler mHandler = new Handler();
    private Runnable mStartRunnable;
    private Runnable mGetIdRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_god_protocol);

        et = (EditText) findViewById(R.id.editText);
        et2 = (EditText) findViewById(R.id.editText2);
        tv = (TextView) findViewById(R.id.textview);
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setText("水神售水机\n");
        getInfo(2);


        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WaterGodProtocol.this);
                builder.setTitle("清除数据？");
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv.setText("");
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();

                return false;
            }
        });


        mStartRunnable = new Runnable() {
            @Override
            public void run() {
                startProtocol();
            }
        };
        startThread = new Thread(mStartRunnable);
        startThread.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getInfo(2);
            }
        }, 1000L);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getInfo(3);
            }
        }, 1500L);

//        mHandler.post(mStartRunnable);



        final Handler mHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0xA1:
                        mMachineId = new String(getRealData(getRpt(2)));
                        tv.append(appendHead+"机器编码:bytes2HexString=" + bytes2HexString((getRpt(2)))+"=");
                        tv.append("\n");
                        byte[] machineIdByte2 = getRealData(getRpt(2));
                        tv.append(appendHead+"机器编码:getRealData=" + bytes2HexString(machineIdByte2)+"=");
                        tv.append("\n");
                        tv.append(appendHead+"机器编码:String=" + (new String(machineIdByte2))+"=");
                        tv.append("\n");
                        break;
                    case 0xA2:

                        tv.append(appendHead+"固件版本号:bytes2HexString=" + bytes2HexString(getRpt(3)));
                        tv.append("\n");
                        tv.append(appendHead+"固件版本号:getRealData=" + bytes2HexString(getRealData(getRpt(3))));
                        tv.append("\n");
                        tv.append(appendHead+"固件版本号:String=" + (new String(getRealData(getRpt(3))))+"=");
                        tv.append("\n");
                        break;
                    case 0xA3:
                        tv.append(appendHead + "脉冲数:bytes2HexString=" + bytes2HexString(getRpt(4))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "脉冲数:pulesByte2=" + bytes2HexString(getRealData(getRpt(4)))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "脉冲数:String=" + (new String(getRealData(getRpt(4))))+"=");
                        tv.append("\n");
                        break;
                    case 0xA4:

                        tv.append(appendHead + "单次购买水量上限制:bytes2HexString=" + bytes2HexString(getRpt(5))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "单次购买水量上限制:getRealData=" + bytes2HexString(getRealData(getRpt(5)))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "单次购买水量上限制:String=" + (new String(getRealData(getRpt(5))))+"=");
                        tv.append("\n");

                        break;
                    case 0xA5:
                        tv.append(appendHead + "总计出水量:bytes2HexString=" + bytes2HexString(getRpt(6))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "总计出水量:getRealData=" + bytes2HexString(getRealData(getRpt(6)))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "总计出水量:String" + (new String(getRealData(getRpt(6))))+"=");
                        tv.append("\n");
                        break;
                    case 0xA6:
                        tv.append(appendHead + "购买水量等待时间:bytes2HexString=" + bytes2HexString(getRpt(7))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "购买水量等待时间:getRealData=" + bytes2HexString(getRealData(getRpt(7)))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "购买水量等待时间:String=" + new String(getRealData(getRpt(7)))+"=");
                        tv.append("\n");
                        break;
                    case 0xA7:
                        tv.append(appendHead + "排水等待时间:bytes2HexString=" + bytes2HexString(getRpt(8))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "排水等待时间:getRealData=" + bytes2HexString(getRealData(getRpt(8)))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "排水等待时间:String=" + new String(getRealData(getRpt(8)))+"=");
                        tv.append("\n");
                        break;
                    case 0xA8:
                        int aF = 0xFF;
                        tv.append(appendHead + "所有值:bytes2HexString！！！=" + bytes2HexString(getRpt(aF))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "所有值:getRealData=" + bytes2HexString((getRpt(aF)))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "所有值:String=" + new String((getRpt(aF)))+"=");
                        tv.append("\n");
                        break;


                    case 0xB1:
                        tv.append(appendHead + "销售状态:bytes2HexString=" + bytes2HexString(getStatusRpt(2))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "销售状态:getRealData=" + bytes2HexString(getRealData(getStatusRpt(2)))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "销售状态:String=" + new String(getRealData(getStatusRpt(2)))+"=");
                        tv.append("\n");
                        break;
                    case 0xB2:
                        tv.append(appendHead + "门开关:bytes2HexString=" + bytes2HexString(getStatusRpt(3))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "门开关:getRealData=" + bytes2HexString(getRealData(getStatusRpt(3)))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "门开关:String=" +  new String(getRealData(getStatusRpt(3)))+"=");
                        tv.append("\n");
                        break;
                    case 0xB3:
                        liquid = bytes2HexString(getStatusRpt(4));
                        tv.append(appendHead + "原液低位:bytes2HexString=" + bytes2HexString(getStatusRpt(4))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "原液低位:getRealData=" + bytes2HexString(getRealData(getStatusRpt(4)))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "原液低位:String=" + new String(getRealData(getStatusRpt(4)))+"=");
                        tv.append("\n");
                        break;
                    case 0xB4:
                        tv.append(appendHead + "低水压:bytes2HexString=" + bytes2HexString(getStatusRpt(5))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "低水压:getRealData=" + bytes2HexString(getRealData(getStatusRpt(5)))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "低水压:String=" + new String(getRealData(getStatusRpt(5)))+"=");
                        tv.append("\n");
                        break;
                    case 0xB5:
                        tv.append(appendHead + "F2断开:bytes2HexString=" + bytes2HexString(getStatusRpt(6))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "F2断开:getRealData=" + bytes2HexString(getRealData(getStatusRpt(6)))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "F2断开:String=" + new String(getRealData(getStatusRpt(6)))+"=");
                        tv.append("\n");
                        break;
                    case 0xB6:
                        tv.append(appendHead + "machineoutput1:bytes2HexString=" + bytes2HexString(getStatusRpt(7))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "machineoutput1:getRealData=" + bytes2HexString(getRealData(getStatusRpt(7)))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "machineoutput1:String=" + new String(getRealData(getStatusRpt(7)))+"=");
                        tv.append("\n");
                        break;
                    case 0xB7:
                        tv.append(appendHead + "machineoutput2:bytes2HexString=" + bytes2HexString(getStatusRpt(8))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "machineoutput2:getRealData=" + bytes2HexString(getRealData(getStatusRpt(8)))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "machineoutput2:String=" +  new String(getRealData(getStatusRpt(8)))+"=");
                        tv.append("\n");
                        break;
                    case 0xB8:
                        tv.append(appendHead + "statusAll:bytes2HexString=" + bytes2HexString(getStatusRpt(0xFF))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "statusAll:getRealData=" + bytes2HexString(getRealData(getStatusRpt(0xFF)))+"=");
                        tv.append("\n");
                        tv.append(appendHead + "statusAll:String=" +  new String(getRealData(getStatusRpt(0xFF)))+"=");
                        tv.append("\n");
                        break;
                    case 0xC1:
                        tv.append(appendHead+"VMC启动完成！");
                        tv.append("\n");
                        break;
                    case 0xD1:
                        tv.append(appendHead + "D1:出水成功后接收到的信息");
                        tv.append(appendHead + "出水信息"+bytes2HexString(getVenderActionStatus()));
                        tv.append("\n");
//                        count ++;
//                        if(count==1){
//                            setVenderAction(0x0A);
//                        }
                        break;
                    case 0xE1://read
                        tv.append(appendHead + "E1");
                        tv.append(appendHead+"read="+new String(getRealData(getCommStatusRpt(1)))+"==");
                        tv.append("\n");
                        break;
                    case 0xE2://write
                        tv.append(appendHead + "E2");
                        tv.append(appendHead+"write="+bytes2HexString(getCommStatusRpt(2))+"=");
                        tv.append("\n");
                        break;



                }
            }
        };


        mGetIdRunnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!isBreak) break;
                    int i;
                    i = getEvent();
                    mHandler.sendEmptyMessage(i);
                }
            }
        };
        getIdThread = new Thread(mGetIdRunnable);
        getIdThread.start();
//        mHandler.post(mGetIdRunnable);
    }


    public static String bytes2HexString(byte[] b) {        //byte转16进制字符串函数
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }


    public void getStatusButtonClicked(View view) {
        int type = Integer.parseInt(et.getText().toString());
        getStatus(type);
        tv.append("PC请求getStatus,类型" + type);
        tv.append("\n");
    }


    public void getInfoButtonClicked(View view) {
        int type = Integer.parseInt(et.getText().toString());
        getInfo(type);
        tv.append("PC请求getInfo,类型" + type);
        tv.append("\n");
    }

    public void setMachineIdButtonClicked(View view) {

//        byte machineId[] = {'1','1','1','1','1','1','1','1'};
        byte machineId[] = et.getText().toString().getBytes();
        Log.e("tag:","设置机器ID");
       int resu =  setMachineID(machineId);
        tv.append("PC请求设置机器编号 resu="+resu);
        tv.append("\n");
    }


    public void setFlowControlerButtonClicked(View view) {
        String getText = et.getText().toString();
//        byte waterFlow[] = et.getText().toString().getBytes();

//        byte waterFlow[] = {(byte)0x01,(byte)0x90};

//        byte waterFlow[] = Integer.toHexString(Integer.parseInt(getText)).getBytes();

        byte waterFlow[] = HexString2Bytes(Integer.toHexString(Integer.parseInt(getText)));

        setFlowControler(waterFlow);
        tv.append("PC请求设置脉冲流量计" + Arrays.toString(waterFlow));
        tv.append("\n");
    }


//    public void setMaxlitreButtonClicked(View view) {
//        byte waterLitre = 0x01;
//        setMaxLitre(waterLitre);
//        tv.append("PC请求设置购买水量上线" + waterLitre );
//        tv.append("\n");
//    }



    public void set1LButtonClicked(View view) {
        int nLitre;
        nLitre = 0x0A;
        Log.e("tag:", "出水1L");
        setVenderAction(nLitre);
    }

    public void set2LButtonClicked(View view) {
        int nLitre;
        nLitre = 0x14;
        setVenderAction(nLitre);
    }

    public void set3LButtonClicked(View view) {
        int nLitre;
        nLitre = 0x1a;
        setVenderAction(nLitre);
    }

    public void set4LButtonClicked(View view) {
        int nLitre;
        nLitre = 0x28;
        setVenderAction(nLitre);
    }

    public void set5LButtonClicked(View view) {
        char nLitre;
        nLitre = 0x32;
        setVenderAction(nLitre);
    }

    public void setLitreAndTimeButtonClicked(View view) {
        String litre = et.getText().toString();
        String time = et2.getText().toString();
        tv.append("PC请求设置水和时间" + " 水="+litre+" 时间="+time+" type=5、7\n");
//        tv.append("PC请求设置水和时间" + " 水=10"+" 时间=10"+" type=5、7\n");

        setMaxLitreAndTime(Byte.parseByte(litre),Byte.parseByte(time));
    }

    public void testLiquidButtonClicked(View view) {
        getStatus(4);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                tv.append(liquid);
            }
        }, 1000L);
    }


    public void btnInfoFF(View view) {
        int af = 0xFF;
        getInfo(af);
        tv.append("PC请求getInfo,类型 FF" );
        tv.append("\n");

//        isBreak = !isBreak;
//        if(!isBreak){
//            mHandler.removeCallbacks(mStartRunnable);
//            mHandler.removeCallbacks(mGetIdRunnable);
//        }else{
//            mHandler.post(mStartRunnable);
//            mHandler.post(mGetIdRunnable);
//        }


    }

    public void btnStatusFF(View view) {
        int af = 0xFF;
        getStatus(af);
        tv.append("PC请求getStatus,类型 FF" );
        tv.append("\n");

    }



    /**
     * 16进制字符串转byte数组
     * @param hexString 16进制字符串
     * @return byte数组
     */
    public static byte[] HexString2Bytes(String hexString) {
        int stringLength = hexString.length();
        //新加判断
        if((stringLength%2)==1){
//            如果是奇数
            hexString = "0"+hexString;
            stringLength +=1;
        }
        //
        byte[] data = new byte[(stringLength / 2)];
        for (int i = 0, j = 0; i < data.length; i++, j = j + 2) {
            data[i] = (byte) Integer.parseInt(hexString.substring(j, (j + 2)), 16);
        }
        return data;
    }

    private byte[] getRealData(byte[] src){
        byte[] returnByte = new byte[src.length-1];
        System.arraycopy(src,1,returnByte,0,src.length-1);
        return returnByte;
    }

    public void btnCom(View view) {
        int commInt = Integer.parseInt(et.getText().toString());
        getCommStatus(commInt);
    }



    public void btnGetMachineId(View view) {
        getInfo(2);
        tv.append("获取机器ID="+mMachineId);
        tv.append("\n");

    }

    public void btnTestGetInfo(View view) {
//        Log.e(TAG,"=="+tv.getText().toString());
        getInfo(2);
        getInfo(3);

    }

    public void testWaittingTime(View view) {

        setPumpTime(Integer.parseInt(et.getText().toString()));
    }








    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native int getEvent();

    public native void getStatus(int m_type);

    public native void getInfo(int m_type);

    public native int setMachineID(byte[] machine_id);

//    public native byte[] getMachineId();

//    public native  byte[] getFlowControler();

//    public native  byte[] getMaxLitreAndTime();

    public native void setFlowControler(byte[] waterFlow);

    public native void setMaxLitreAndTime(byte Litre ,byte waterTime);

    public native byte[] getRpt(int rpt_type);

    public native byte[] getStatusRpt(int rpt_type);

    public native void setVenderAction(int nLitre);

    public native int startProtocol();

//    public native void SetReset();

    public native byte[] getVenderActionStatus();

    public native void getCommStatus(int type);

//    public native int getCommStatusRpt(int rpt_type);//0 正常    1不正常

    public native byte[] getCommStatusRpt(int rpt_type);

    public native int setPumpTime(int nTime);

}

