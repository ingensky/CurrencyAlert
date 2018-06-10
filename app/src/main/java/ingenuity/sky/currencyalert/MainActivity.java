package ingenuity.sky.currencyalert;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

public class MainActivity extends AppCompatActivity {
    static ProgressBar progressBar;
    static TextView usdText;
    static EditText usdEditMin;
    static EditText usdEditMax;
    static Switch usdSwitch;
    static TextView XBTText;
    static EditText XBTEditMin;
    static EditText XBTEditMax;
    static Switch XBTSwitch;
    static TextView EURText;
    static EditText EUREditMin;
    static EditText EUREditMax;
    static Switch EURSwitch;
    static TextView OILText;
    static EditText OILEditMin;
    static EditText OILEditMax;
    static Switch OILSwitch;
    static TextView RUBText;
    static EditText RUBEditMin;
    static EditText RUBEditMax;
    static Switch RUBSwitch;
    static TextView ETHText;
    static EditText ETHEditMin;
    static EditText ETHEditMax;
    static Switch ETHSwitch;

    static TextView textViewMainCurrency;
    static SharedPreferences localPreferences;
    static ToggleButton toggleButton;


    static AlarmEventReceiver alarmEventReceiver = new AlarmEventReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);


        localPreferencesInit(getApplicationContext());


        usdUIInit();
        XBTUIInit();
        EURUIInit();
        OILUIInit();
        RUBUIInit();
        ETHUIInit();


        textViewMainCurrency = (TextView) findViewById(R.id.textView);
        textViewMainCurrency.setText(localPreferences.getString("to", "RUB"));

        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setChecked(localPreferences.getBoolean("service_on", false));

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                localPreferencesInit(getApplicationContext());
                if (isChecked) {
                    localPreferences.edit().putBoolean("service_on", true).apply();
                    alarmEventReceiver.start(getApplicationContext());
                } else {
                    localPreferences.edit().putBoolean("service_on", false).apply();
                    alarmEventReceiver.stop();

                }

            }
        });

        fadeDataRestore();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    alarmEventReceiver.onReceive(getApplicationContext(), new Intent(getApplicationContext(), MainActivity.class));
                }

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void fadeDataRestore() {
        float usDvalue = localPreferences.getFloat("usdvalue", 0);
        if (usDvalue == 0) {
            usdText.setText("####");
        }
        usdText.setText(String.valueOf(usDvalue));
        usdText.setTextColor(Color.GRAY);


        float euRvalue = localPreferences.getFloat("EURvalue", 0);
        if (euRvalue == 0) {
            EURText.setText("####");
        }
        EURText.setText(String.valueOf(euRvalue));
        EURText.setTextColor(Color.GRAY);

        float ruBvalue = localPreferences.getFloat("RUBvalue", 0);
        if (ruBvalue == 0) {
            RUBText.setText("####");
        }
        RUBText.setText(String.valueOf(ruBvalue));
        RUBText.setTextColor(Color.GRAY);

        float xbTvalue = localPreferences.getFloat("XBTvalue", 0);
        if (xbTvalue == 0) {
            XBTText.setText("####");
        }
        XBTText.setText(String.valueOf(xbTvalue));
        XBTText.setTextColor(Color.GRAY);

        float etHvalue = localPreferences.getFloat("ETHvalue", 0);
        if (etHvalue == 0) {
            ETHText.setText("####");
        }
        ETHText.setText(String.valueOf(etHvalue));
        ETHText.setTextColor(Color.GRAY);

        float oiLvalue = localPreferences.getFloat("OILvalue", 0);
        if (oiLvalue == 0) {
            OILText.setText("####");
        }
        OILText.setText(String.valueOf(oiLvalue));
        OILText.setTextColor(Color.GRAY);
    }

    private void usdUIInit() {
        usdText = (TextView) findViewById(R.id.usdValueText);
        usdEditMin = (EditText) findViewById(R.id.editTextUsdMin);
        float usdmin = localPreferences.getFloat("usdmin", 0);
        usdEditMin.setText(String.valueOf(usdmin));
        usdEditMin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                usdDisactivate(v);
                return false;
            }
        });

        usdEditMax = (EditText) findViewById(R.id.editTextUsdMax);
        float usdmax = localPreferences.getFloat("usdmax", 99999);
        usdEditMax.setText(String.valueOf(usdmax));
        usdEditMax.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                usdDisactivate(v);
                return false;
            }
        });
        usdSwitch = (Switch) findViewById(R.id.switch1);
        usdSwitch.setChecked(localPreferences.getBoolean("usd_is_active", false));

        usdSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    USD.isActive = true;
                    localPreferences.edit().putBoolean("usd_is_active", true).apply();
                    localPreferences.edit().putFloat("usdmin", (float) Double.parseDouble(usdEditMin.getText().toString())).apply();

                    localPreferences.edit().putFloat("usdmax", (float) Double.parseDouble(usdEditMax.getText().toString())).apply();

                } else {
                    USD.isActive = false;
                    localPreferences.edit().putBoolean("usd_is_active", false).apply();

                }
            }
        });
    }


    private void XBTUIInit() {
        XBTText = (TextView) findViewById(R.id.XBTValueText);
        XBTEditMin = (EditText) findViewById(R.id.editTextXBTMin);
        float XBTmin = localPreferences.getFloat("XBTmin", 0);
        XBTEditMin.setText(String.valueOf(XBTmin));
        XBTEditMin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                XBTDisactivate(v);
                return false;
            }

        });

        XBTEditMax = (EditText) findViewById(R.id.editTextXBTMax);
        float XBTmax = localPreferences.getFloat("XBTmax", 99999);
        XBTEditMax.setText(String.valueOf(XBTmax));
        XBTEditMax.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                XBTDisactivate(v);
                return false;
            }
        });
        XBTSwitch = (Switch) findViewById(R.id.switchXBT);
        XBTSwitch.setChecked(localPreferences.getBoolean("XBT_is_active", false));

        XBTSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    XBT.isActive = true;
                    localPreferences.edit().putBoolean("XBT_is_active", true).apply();
                    localPreferences.edit().putFloat("XBTmin", (float) Double.parseDouble(XBTEditMin.getText().toString())).apply();

                    localPreferences.edit().putFloat("XBTmax", (float) Double.parseDouble(XBTEditMax.getText().toString())).apply();

                } else {
                    XBT.isActive = false;
                    localPreferences.edit().putBoolean("XBT_is_active", false).apply();

                }
            }
        });
    }

    private void ETHUIInit() {
        ETHText = (TextView) findViewById(R.id.ETHValueText);
        ETHEditMin = (EditText) findViewById(R.id.editTextETHMin);
        float ETHmin = localPreferences.getFloat("ETHmin", 0);
        ETHEditMin.setText(String.valueOf(ETHmin));
        ETHEditMin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ETHDisactivate(v);
                return false;
            }

        });

        ETHEditMax = (EditText) findViewById(R.id.editTextETHMax);
        float ETHmax = localPreferences.getFloat("ETHmax", 99999);
        ETHEditMax.setText(String.valueOf(ETHmax));
        ETHEditMax.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ETHDisactivate(v);
                return false;
            }
        });
        ETHSwitch = (Switch) findViewById(R.id.switchETH);
        ETHSwitch.setChecked(localPreferences.getBoolean("ETH_is_active", false));

        ETHSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ETH.isActive = true;
                    localPreferences.edit().putBoolean("ETH_is_active", true).apply();
                    localPreferences.edit().putFloat("ETHmin", (float) Double.parseDouble(ETHEditMin.getText().toString())).apply();

                    localPreferences.edit().putFloat("ETHmax", (float) Double.parseDouble(ETHEditMax.getText().toString())).apply();

                } else {
                    ETH.isActive = false;
                    localPreferences.edit().putBoolean("ETH_is_active", false).apply();

                }
            }
        });
    }

    private void OILUIInit() {
        OILText = (TextView) findViewById(R.id.OILValueText);
        OILEditMin = (EditText) findViewById(R.id.editTextOILMin);
        float OILmin = localPreferences.getFloat("OILmin", 0);
        OILEditMin.setText(String.valueOf(OILmin));
        OILEditMin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                OILDisactivate(v);
                return false;
            }

        });

        OILEditMax = (EditText) findViewById(R.id.editTextOILMax);
        float OILmax = localPreferences.getFloat("OILmax", 99999);
        OILEditMax.setText(String.valueOf(OILmax));
        OILEditMax.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                OILDisactivate(v);
                return false;
            }
        });
        OILSwitch = (Switch) findViewById(R.id.switchOIL);
        OILSwitch.setChecked(localPreferences.getBoolean("OIL_is_active", false));

        OILSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    OIL.isActive = true;
                    localPreferences.edit().putBoolean("OIL_is_active", true).apply();
                    localPreferences.edit().putFloat("OILmin", (float) Double.parseDouble(OILEditMin.getText().toString())).apply();

                    localPreferences.edit().putFloat("OILmax", (float) Double.parseDouble(OILEditMax.getText().toString())).apply();

                } else {
                    OIL.isActive = false;
                    localPreferences.edit().putBoolean("OIL_is_active", false).apply();

                }
            }
        });
    }


    private void EURUIInit() {
        EURText = (TextView) findViewById(R.id.EURValueText);
        EUREditMin = (EditText) findViewById(R.id.editTextEURMin);
        float EURmin = localPreferences.getFloat("EURmin", 0);
        EUREditMin.setText(String.valueOf(EURmin));
        EUREditMin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EURDisactivate(v);
                return false;
            }

        });

        EUREditMax = (EditText) findViewById(R.id.editTextEURMax);
        float EURmax = localPreferences.getFloat("EURmax", 99999);
        EUREditMax.setText(String.valueOf(EURmax));
        EUREditMax.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EURDisactivate(v);
                return false;
            }
        });
        EURSwitch = (Switch) findViewById(R.id.switchEUR);
        EURSwitch.setChecked(localPreferences.getBoolean("EUR_is_active", false));

        EURSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    EUR.isActive = true;
                    localPreferences.edit().putBoolean("EUR_is_active", true).apply();
                    localPreferences.edit().putFloat("EURmin", (float) Double.parseDouble(EUREditMin.getText().toString())).apply();

                    localPreferences.edit().putFloat("EURmax", (float) Double.parseDouble(EUREditMax.getText().toString())).apply();

                } else {
                    EUR.isActive = false;
                    localPreferences.edit().putBoolean("EUR_is_active", false).apply();

                }
            }
        });
    }

    private void RUBUIInit() {
        RUBText = (TextView) findViewById(R.id.RUBValueText);
//        RUBText.setBackgroundColor(Color.WHITE);
        RUBEditMin = (EditText) findViewById(R.id.editTextRUBMin);
        float RUBmin = localPreferences.getFloat("RUBmin", 0);
        RUBEditMin.setText(String.valueOf(RUBmin));
        RUBEditMin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RUBDisactivate(v);
                return false;
            }

        });

        RUBEditMax = (EditText) findViewById(R.id.editTextRUBMax);
        float RUBmax = localPreferences.getFloat("RUBmax", 99999);
        RUBEditMax.setText(String.valueOf(RUBmax));
        RUBEditMax.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RUBDisactivate(v);
                return false;
            }
        });
        RUBSwitch = (Switch) findViewById(R.id.switchRUB);
        RUBSwitch.setChecked(localPreferences.getBoolean("RUB_is_active", false));

        RUBSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    RUB.isActive = true;
                    localPreferences.edit().putBoolean("RUB_is_active", true).apply();
                    localPreferences.edit().putFloat("RUBmin", (float) Double.parseDouble(RUBEditMin.getText().toString())).apply();

                    localPreferences.edit().putFloat("RUBmax", (float) Double.parseDouble(RUBEditMax.getText().toString())).apply();

                } else {
                    RUB.isActive = false;
                    localPreferences.edit().putBoolean("RUB_is_active", false).apply();

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        localPreferencesInit(getApplicationContext());


        int period = localPreferences.getInt("period", 180);
        switch (period) {
            case 1:
                menu.getItem(1).getSubMenu().getItem(0).setChecked(true);
                break;
            case 5:
                menu.getItem(1).getSubMenu().getItem(1).setChecked(true);
                break;

            case 15:
                menu.getItem(1).getSubMenu().getItem(2).setChecked(true);
                break;

            case 30:
                menu.getItem(1).getSubMenu().getItem(3).setChecked(true);
                break;

            case 60:
                menu.getItem(1).getSubMenu().getItem(4).setChecked(true);
                break;
            case 120:
                menu.getItem(1).getSubMenu().getItem(5).setChecked(true);
                break;
            case 180:
                menu.getItem(1).getSubMenu().getItem(6).setChecked(true);
                break;

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        localPreferencesInit(getApplicationContext());

        switch (id) {
            case R.id.setRub:
                textViewMainCurrency.setText("RUB");
                localPreferences.edit().putString("to", "RUB").apply();
                break;
            case R.id.setHriv:
                textViewMainCurrency.setText("UAH");
                localPreferences.edit().putString("to", "UAH").apply();
                break;
            case R.id.setBel:
                textViewMainCurrency.setText("BYN");
                localPreferences.edit().putString("to", "BYN").apply();
                break;
            case R.id.setKZT:
                textViewMainCurrency.setText("KZT");
                localPreferences.edit().putString("to", "KZT").apply();
                break;
            case R.id.setUsd:
                textViewMainCurrency.setText("USD");
                localPreferences.edit().putString("to", "USD").apply();
                break;
            case R.id.setEur:
                textViewMainCurrency.setText("EUR");
                localPreferences.edit().putString("to", "EUR").apply();
                break;
            case R.id.set1m:
                localPreferences.edit().putInt("period", 1).apply();
                item.setChecked(true);
                break;
            case R.id.set5m:
                localPreferences.edit().putInt("period", 5).apply();
                item.setChecked(true);
                break;
            case R.id.set15m:
                localPreferences.edit().putInt("period", 15).apply();
                item.setChecked(true);
                break;
            case R.id.set30m:
                localPreferences.edit().putInt("period", 30).apply();
                item.setChecked(true);
                break;
            case R.id.set1h:
                localPreferences.edit().putInt("period", 60).apply();
                item.setChecked(true);
                break;
            case R.id.set2h:
                localPreferences.edit().putInt("period", 120).apply();
                item.setChecked(true);
                break;
            case R.id.set3h:
                localPreferences.edit().putInt("period", 180).apply();
                item.setChecked(true);
                break;
            case R.id.setSoundOff:
                localPreferences.edit().putInt("sound", 1).apply();
                break;
            case R.id.setSoundCoin:
                localPreferences.edit().putInt("sound", 2).apply();
                break;
            case R.id.setSoundStan:
                localPreferences.edit().putInt("sound", 3).apply();
                break;
            case R.id.setVibroOff:
                localPreferences.edit().putInt("vibro", 1).apply();
                break;
            case R.id.setVibroShort:
                localPreferences.edit().putInt("vibro", 2).apply();
                break;
            case R.id.setVibroStan:

                localPreferences.edit().putInt("vibro", 3).apply();
                break;
            case R.id.setDiodOff:
                localPreferences.edit().putInt("diod", 1).apply();
                break;
            case R.id.setDiodOn:
                localPreferences.edit().putInt("diod", 2).apply();
                break;
        }


        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    private void usdDisactivate(View v) {

        usdSwitch.setChecked(false);
        localPreferences.edit().putBoolean("usd_is_active", false).apply();

    }

    private void OILDisactivate(View v) {
        OILSwitch.setChecked(false);
        localPreferences.edit().putBoolean("OIL_is_active", false).apply();

    }


    private void EURDisactivate(View v) {
        EURSwitch.setChecked(false);
        localPreferences.edit().putBoolean("EUR_is_active", false).apply();


    }


    private void XBTDisactivate(View v) {

        XBTSwitch.setChecked(false);
        localPreferences.edit().putBoolean("XBT_is_active", false).apply();
 

    }

    private void ETHDisactivate(View v) {
        ETHSwitch.setChecked(false);
        localPreferences.edit().putBoolean("ETH_is_active", false).apply();


    }

    private void RUBDisactivate(View v) {
        RUBSwitch.setChecked(false);
        localPreferences.edit().putBoolean("RUB_is_active", false).apply();

    }


    private void localPreferencesInit(Context context) {
        if (localPreferences == null) {
            localPreferences = context.getSharedPreferences("mainPreferences", Context.MODE_PRIVATE);
        }
    }

    public void getHelpClick(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Справка")
                .setIcon(R.mipmap.ic_launcher)

                .setMessage("RUB - рус. рубль (отображет отношенеие выбранной валюты к рус. рублю, для KZT -  обратное)\r\n" +
                        "UAH - гривна\n" +
                        "BYN - бел. рубль\n" +
                        "KZT - казах. тенге\n" +
                        "USD - доллар США\r\n" +
                        "EUR - евро\n" +
                        "XBT$ - Bitcoin в долларах США\n" +
                        "ETH$ - Ethereum в долларах США\n" +
                        "OIL$ - нефть Brent в долларах СШA\n" +
                        "\n" +
                        "Внимание: Не забудьте добавить это приложение в исключения вашего менеджера батареи и программы запрещающей автозагрузку для приложений, если таковые имеются.")
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
