package indoorgeolocation.aps.uvigo;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "indoorgeolocation.aps.uvigo", "indoorgeolocation.aps.uvigo.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "indoorgeolocation.aps.uvigo", "indoorgeolocation.aps.uvigo.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "indoorgeolocation.aps.uvigo.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create " + (isFirst ? "(first time)" : "") + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (main) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static com.airlinemates.mlwifi.MLwifi _wifiobject = null;
public static com.airlinemates.mlwifi.MLwifi.MLWifiScanner _wifiscan = null;
public static igolub.permissionmanager.b4a.PermissionsManager _pmanager = null;
public static anywheresoftware.b4a.phone.Phone _phone = null;
public static anywheresoftware.b4a.objects.collections.Map _config1 = null;
public static anywheresoftware.b4a.objects.collections.Map _datapoint = null;
public static anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public static String _androidid = "";
public static long _tickcountexit = 0L;
public static int _erroresreconectando = 0;
public anywheresoftware.b4a.objects.ButtonWrapper _btnconfig = null;
public indoorgeolocation.aps.uvigo.preferencesdialog _prefdialog = null;
public static String _configfile = "";
public anywheresoftware.b4a.agraham.byteconverter.ByteConverter _conv = null;
public anywheresoftware.b4j.objects.MqttAsyncClientWrapper _client = null;
public static boolean _connectedmqtt = false;
public anywheresoftware.b4a.objects.LabelWrapper _lblinfo = null;
public anywheresoftware.b4a.objects.Timer _tmrmqttreconnect = null;
public anywheresoftware.b4a.objects.Timer _tmrautoscan = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnscan = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblsend = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblscan = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbldisconnect = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblconnect = null;
public static boolean _scanningwifi = false;
public static int _counter = 0;
public static int _countersec = 0;
public anywheresoftware.b4a.objects.ImageViewWrapper _lbliconwifi = null;
public static String _client_id = "";
public igolub.permissionmanager.b4a.PermissionsManagerFileProvider _fileprovider = null;
public igolub.permissionmanager.b4a.PermissionsManagerConstants _permissionsmanager = null;
public b4a.example.dateutils _dateutils = null;
public indoorgeolocation.aps.uvigo.starter _starter = null;
public indoorgeolocation.aps.uvigo.xuiviewsutils _xuiviewsutils = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static class _tregistro{
public boolean IsInitialized;
public String fabricante;
public String modelo;
public String producto;
public String sdk;
public String androidID;
public long fecha_hora;
public String nivel;
public int punto;
public int counter;
public void Initialize() {
IsInitialized = true;
fabricante = "";
modelo = "";
producto = "";
sdk = "";
androidID = "";
fecha_hora = 0L;
nivel = "";
punto = 0;
counter = 0;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static class _twifiscan{
public boolean IsInitialized;
public String SSID;
public String BSSID;
public String seguridad;
public int potencia;
public int canal;
public double distancia;
public int data_0;
public int data_1;
public int data_2;
public int data_3;
public int data_4;
public int data_5;
public int data_6;
public int data_7;
public int data_8;
public int data_9;
public void Initialize() {
IsInitialized = true;
SSID = "";
BSSID = "";
seguridad = "";
potencia = 0;
canal = 0;
distancia = 0;
data_0 = 0;
data_1 = 0;
data_2 = 0;
data_3 = 0;
data_4 = 0;
data_5 = 0;
data_6 = 0;
data_7 = 0;
data_8 = 0;
data_9 = 0;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 55;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 56;BA.debugLine="Activity.LoadLayout(\"LytMain\")";
mostCurrent._activity.LoadLayout("LytMain",mostCurrent.activityBA);
 //BA.debugLineNum = 57;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 58;BA.debugLine="CheckAndRequestPermissions";
_checkandrequestpermissions();
 };
 //BA.debugLineNum = 60;BA.debugLine="setStateMQTT";
_setstatemqtt();
 //BA.debugLineNum = 61;BA.debugLine="setStateScan";
_setstatescan();
 //BA.debugLineNum = 62;BA.debugLine="androidID=GetAndroidID";
_androidid = _getandroidid();
 //BA.debugLineNum = 63;BA.debugLine="tmrMQTTReconnect.Initialize(\"tmrMQTTReconnect\",10";
mostCurrent._tmrmqttreconnect.Initialize(processBA,"tmrMQTTReconnect",(long) (1000));
 //BA.debugLineNum = 64;BA.debugLine="tmrAutoScan.Initialize(\"tmrAutoScan\",1000)";
mostCurrent._tmrautoscan.Initialize(processBA,"tmrAutoScan",(long) (1000));
 //BA.debugLineNum = 65;BA.debugLine="tmrMQTTReconnect.Enabled=True";
mostCurrent._tmrmqttreconnect.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 66;BA.debugLine="tmrAutoScan.Enabled=True";
mostCurrent._tmrautoscan.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 67;BA.debugLine="lblSend.Text=\"\"";
mostCurrent._lblsend.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 68;BA.debugLine="CLIENT_ID = $\"APS_${DateTime.Now Mod 100000}\"$";
mostCurrent._client_id = ("APS_"+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(anywheresoftware.b4a.keywords.Common.DateTime.getNow()%100000))+"");
 //BA.debugLineNum = 69;BA.debugLine="If CLIENT_ID.Length > 23 Then";
if (mostCurrent._client_id.length()>23) { 
 //BA.debugLineNum = 70;BA.debugLine="CLIENT_ID = CLIENT_ID.SubString2(0, 23)";
mostCurrent._client_id = mostCurrent._client_id.substring((int) (0),(int) (23));
 };
 //BA.debugLineNum = 72;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
int _answ = 0;
 //BA.debugLineNum = 83;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 85;BA.debugLine="Dim Answ As Int";
_answ = 0;
 //BA.debugLineNum = 86;BA.debugLine="Select KeyCode";
switch (BA.switchObjectToInt(_keycode,anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK)) {
case 0: {
 //BA.debugLineNum = 88;BA.debugLine="If (DateTime.Now-tickCountExit)<2000 Then";
if ((anywheresoftware.b4a.keywords.Common.DateTime.getNow()-_tickcountexit)<2000) { 
 //BA.debugLineNum = 89;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 }else {
 //BA.debugLineNum = 91;BA.debugLine="tickCountExit=DateTime.Now";
_tickcountexit = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 92;BA.debugLine="ToastMessageShow(\"Doble pulsación para salir\"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Doble pulsación para salir"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 93;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 break; }
}
;
 //BA.debugLineNum = 98;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 80;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 82;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 74;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 75;BA.debugLine="If LoadConfig Then";
if (_loadconfig()) { 
 //BA.debugLineNum = 76;BA.debugLine="connect_MQTT";
_connect_mqtt();
 };
 //BA.debugLineNum = 78;BA.debugLine="End Sub";
return "";
}
public static void  _btnconfig_click() throws Exception{
ResumableSub_btnConfig_Click rsub = new ResumableSub_btnConfig_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnConfig_Click extends BA.ResumableSub {
public ResumableSub_btnConfig_Click(indoorgeolocation.aps.uvigo.main parent) {
this.parent = parent;
}
indoorgeolocation.aps.uvigo.main parent;
int _result = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 272;BA.debugLine="If connectedMQTT Then client.Close";
if (true) break;

case 1:
//if
this.state = 6;
if (parent._connectedmqtt) { 
this.state = 3;
;}if (true) break;

case 3:
//C
this.state = 6;
parent.mostCurrent._client.Close();
if (true) break;

case 6:
//C
this.state = 7;
;
 //BA.debugLineNum = 273;BA.debugLine="LoadConfig";
_loadconfig();
 //BA.debugLineNum = 274;BA.debugLine="tmrMQTTReconnect.Enabled=False";
parent.mostCurrent._tmrmqttreconnect.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 275;BA.debugLine="tmrAutoScan.Enabled=False";
parent.mostCurrent._tmrautoscan.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 276;BA.debugLine="Wait For (prefdialog.ShowDialog(Config1, \"OK\", \"C";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, parent.mostCurrent._prefdialog._showdialog /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ (parent._config1,(Object)("OK"),(Object)("CANCELAR")));
this.state = 11;
return;
case 11:
//C
this.state = 7;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 277;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 7:
//if
this.state = 10;
if (_result==parent._xui.DialogResponse_Positive) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 278;BA.debugLine="SaveConfig(Config1)";
_saveconfig(parent._config1);
 //BA.debugLineNum = 279;BA.debugLine="btnScan.Enabled=Not(Config1.Get(\"keyAutoFind\"))";
parent.mostCurrent._btnscan.setEnabled(anywheresoftware.b4a.keywords.Common.Not(BA.ObjectToBoolean(parent._config1.Get((Object)("keyAutoFind")))));
 //BA.debugLineNum = 280;BA.debugLine="lblScan.Visible=False";
parent.mostCurrent._lblscan.setVisible(anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 282;BA.debugLine="ErroresReconectando=0";
parent._erroresreconectando = (int) (0);
 //BA.debugLineNum = 283;BA.debugLine="counter=0";
parent._counter = (int) (0);
 //BA.debugLineNum = 284;BA.debugLine="connect_MQTT";
_connect_mqtt();
 //BA.debugLineNum = 285;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _complete(int _result) throws Exception{
}
public static String  _btnscan_click() throws Exception{
 //BA.debugLineNum = 234;BA.debugLine="Sub btnScan_Click";
 //BA.debugLineNum = 235;BA.debugLine="scanAP";
_scanap();
 //BA.debugLineNum = 236;BA.debugLine="End Sub";
return "";
}
public static void  _checkandrequestpermissions() throws Exception{
ResumableSub_CheckAndRequestPermissions rsub = new ResumableSub_CheckAndRequestPermissions(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_CheckAndRequestPermissions extends BA.ResumableSub {
public ResumableSub_CheckAndRequestPermissions(indoorgeolocation.aps.uvigo.main parent) {
this.parent = parent;
}
indoorgeolocation.aps.uvigo.main parent;
int _sdkversion = 0;
String _permission = "";
boolean _result = false;
anywheresoftware.b4a.BA.IterableList group5;
int index5;
int groupLen5;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 535;BA.debugLine="Dim sdkVersion As Int = phone.SdkVersion";
_sdkversion = parent._phone.getSdkVersion();
 //BA.debugLineNum = 536;BA.debugLine="pmanager.ApplicationResumeAllowed = False";
parent._pmanager.setApplicationResumeAllowed(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 538;BA.debugLine="pmanager.ApplicationResumeAllowed=False";
parent._pmanager.setApplicationResumeAllowed(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 539;BA.debugLine="If sdkVersion >= 23 Then";
if (true) break;

case 1:
//if
this.state = 14;
if (_sdkversion>=23) { 
this.state = 3;
}else {
this.state = 13;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 540;BA.debugLine="For Each permission As String In pmanager.AllPer";
if (true) break;

case 4:
//for
this.state = 11;
group5 = parent._pmanager.getAllPermissionsInManifestFile();
index5 = 0;
groupLen5 = group5.getSize();
this.state = 15;
if (true) break;

case 15:
//C
this.state = 11;
if (index5 < groupLen5) {
this.state = 6;
_permission = BA.ObjectToString(group5.Get(index5));}
if (true) break;

case 16:
//C
this.state = 15;
index5++;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 541;BA.debugLine="pmanager.CheckAndRequestPermission(permission)";
parent._pmanager.CheckAndRequestPermission(processBA,_permission);
 //BA.debugLineNum = 542;BA.debugLine="Wait For Activity_PermissionResult (permission";
anywheresoftware.b4a.keywords.Common.WaitFor("activity_permissionresult", processBA, this, null);
this.state = 17;
return;
case 17:
//C
this.state = 7;
_permission = (String) result[0];
_result = (Boolean) result[1];
;
 //BA.debugLineNum = 543;BA.debugLine="If Result=False Then";
if (true) break;

case 7:
//if
this.state = 10;
if (_result==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 544;BA.debugLine="ToastMessageShow(\"Sin permisos \"&permission, F";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Sin permisos "+_permission),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 545;BA.debugLine="Log(\"Sin permisos \"&permission)";
anywheresoftware.b4a.keywords.Common.LogImpl("32031627","Sin permisos "+_permission,0);
 if (true) break;

case 10:
//C
this.state = 16;
;
 if (true) break;
if (true) break;

case 11:
//C
this.state = 14;
;
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 549;BA.debugLine="Log(\"Versión de Android anterior a 6.0 - permiso";
anywheresoftware.b4a.keywords.Common.LogImpl("32031631","Versión de Android anterior a 6.0 - permisos concedidos en tiempo de instalación.",0);
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 551;BA.debugLine="pmanager.ApplicationResumeAllowed=True 'Stop prev";
parent._pmanager.setApplicationResumeAllowed(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 552;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _activity_permissionresult(String _permission,boolean _result) throws Exception{
}
public static void  _clientmqtt_connected(boolean _success) throws Exception{
ResumableSub_clientMQTT_Connected rsub = new ResumableSub_clientMQTT_Connected(null,_success);
rsub.resume(processBA, null);
}
public static class ResumableSub_clientMQTT_Connected extends BA.ResumableSub {
public ResumableSub_clientMQTT_Connected(indoorgeolocation.aps.uvigo.main parent,boolean _success) {
this.parent = parent;
this._success = _success;
}
indoorgeolocation.aps.uvigo.main parent;
boolean _success;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 383;BA.debugLine="Log($\"Conectado: ${Success}\"$)";
anywheresoftware.b4a.keywords.Common.LogImpl("31572865",("Conectado: "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+""),0);
 //BA.debugLineNum = 384;BA.debugLine="connectedMQTT=Success";
parent._connectedmqtt = _success;
 //BA.debugLineNum = 385;BA.debugLine="If Success Then";
if (true) break;

case 1:
//if
this.state = 18;
if (_success) { 
this.state = 3;
}else {
this.state = 17;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 386;BA.debugLine="ErroresReconectando=0";
parent._erroresreconectando = (int) (0);
 //BA.debugLineNum = 387;BA.debugLine="Sleep(500)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (500));
this.state = 19;
return;
case 19:
//C
this.state = 4;
;
 //BA.debugLineNum = 388;BA.debugLine="If client.Connected Then";
if (true) break;

case 4:
//if
this.state = 15;
if (parent.mostCurrent._client.getConnected()) { 
this.state = 6;
}else {
this.state = 14;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 389;BA.debugLine="If Config1.Get(\"keyAutoFind\") Then";
if (true) break;

case 7:
//if
this.state = 12;
if (BA.ObjectToBoolean(parent._config1.Get((Object)("keyAutoFind")))) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 390;BA.debugLine="client.Subscribe(topicLocationResult, 0)";
parent.mostCurrent._client.Subscribe(_topiclocationresult(),(int) (0));
 //BA.debugLineNum = 391;BA.debugLine="Log(topicLocationResult)";
anywheresoftware.b4a.keywords.Common.LogImpl("31572873",_topiclocationresult(),0);
 //BA.debugLineNum = 392;BA.debugLine="scanAP";
_scanap();
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 394;BA.debugLine="client.Subscribe(topicRegistroResult, 0)";
parent.mostCurrent._client.Subscribe(_topicregistroresult(),(int) (0));
 if (true) break;

case 12:
//C
this.state = 15;
;
 if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 397;BA.debugLine="ToastMessageShow(\"Conexión MQTT no activa\" , Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Conexión MQTT no activa"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 15:
//C
this.state = 18;
;
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 400;BA.debugLine="ErroresReconectando=ErroresReconectando+1";
parent._erroresreconectando = (int) (parent._erroresreconectando+1);
 //BA.debugLineNum = 401;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("31572883",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 402;BA.debugLine="ToastMessageShow(LastException, True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getObject()),anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 18:
//C
this.state = -1;
;
 //BA.debugLineNum = 404;BA.debugLine="setStateMQTT";
_setstatemqtt();
 //BA.debugLineNum = 405;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _clientmqtt_disconnected() throws Exception{
 //BA.debugLineNum = 407;BA.debugLine="Private Sub clientMQTT_Disconnected";
 //BA.debugLineNum = 408;BA.debugLine="Log(\"Desconectado MQTT\")";
anywheresoftware.b4a.keywords.Common.LogImpl("31638401","Desconectado MQTT",0);
 //BA.debugLineNum = 409;BA.debugLine="connectedMQTT=False";
_connectedmqtt = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 410;BA.debugLine="setStateMQTT";
_setstatemqtt();
 //BA.debugLineNum = 412;BA.debugLine="End Sub";
return "";
}
public static String  _clientmqtt_messagearrived(String _topic,byte[] _payload) throws Exception{
String _datos = "";
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _datamap = null;
String _zona = "";
anywheresoftware.b4a.objects.CSBuilder _cs = null;
 //BA.debugLineNum = 419;BA.debugLine="Private Sub clientMQTT_MessageArrived (Topic As St";
 //BA.debugLineNum = 420;BA.debugLine="Dim datos As String = conv.StringFromBytes(Payloa";
_datos = mostCurrent._conv.StringFromBytes(_payload,"UTF8");
 //BA.debugLineNum = 422;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 423;BA.debugLine="parser.Initialize(datos)";
_parser.Initialize(_datos);
 //BA.debugLineNum = 424;BA.debugLine="Try";
try { //BA.debugLineNum = 426;BA.debugLine="Dim dataMap As Map";
_datamap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 427;BA.debugLine="dataMap = parser.NextObject";
_datamap = _parser.NextObject();
 //BA.debugLineNum = 428;BA.debugLine="If Topic.Contains(\"location\") Then";
if (_topic.contains("location")) { 
 //BA.debugLineNum = 430;BA.debugLine="Dim zona As String=dataMap.Get(\"zona_estimada\")";
_zona = BA.ObjectToString(_datamap.Get((Object)("zona_estimada")));
 //BA.debugLineNum = 431;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 432;BA.debugLine="cs.Initialize";
_cs.Initialize();
 //BA.debugLineNum = 434;BA.debugLine="cs.Typeface(Typeface.MATERIALICONS).Append(Chr(";
_cs.Typeface(anywheresoftware.b4a.keywords.Common.Typeface.getMATERIALICONS()).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.Chr(((int)0xe88a)))).PopAll();
 //BA.debugLineNum = 435;BA.debugLine="cs.Append(\" NIVEL: \").PopAll";
_cs.Append(BA.ObjectToCharSequence(" NIVEL: ")).PopAll();
 //BA.debugLineNum = 436;BA.debugLine="cs.Color(Colors.Blue).Append(dataMap.Get(\"nivel";
_cs.Color(anywheresoftware.b4a.keywords.Common.Colors.Blue).Append(BA.ObjectToCharSequence(_datamap.Get((Object)("nivel")))).PopAll();
 //BA.debugLineNum = 437;BA.debugLine="cs.Append(CRLF)";
_cs.Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.CRLF));
 //BA.debugLineNum = 439;BA.debugLine="cs.Typeface(Typeface.MATERIALICONS).Append(Chr(";
_cs.Typeface(anywheresoftware.b4a.keywords.Common.Typeface.getMATERIALICONS()).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.Chr(((int)0xe55a)))).PopAll();
 //BA.debugLineNum = 440;BA.debugLine="cs.Append(\" ZONA ESTIMADA: \").PopAll";
_cs.Append(BA.ObjectToCharSequence(" ZONA ESTIMADA: ")).PopAll();
 //BA.debugLineNum = 441;BA.debugLine="cs.Color(Colors.Blue).Append(zona.ToUpperCase).";
_cs.Color(anywheresoftware.b4a.keywords.Common.Colors.Blue).Append(BA.ObjectToCharSequence(_zona.toUpperCase())).PopAll();
 //BA.debugLineNum = 442;BA.debugLine="cs.Append(CRLF)";
_cs.Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.CRLF));
 //BA.debugLineNum = 444;BA.debugLine="cs.Typeface(Typeface.MATERIALICONS).Append(Chr(";
_cs.Typeface(anywheresoftware.b4a.keywords.Common.Typeface.getMATERIALICONS()).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.Chr(((int)0xe55c)))).PopAll();
 //BA.debugLineNum = 445;BA.debugLine="cs.Append(\" COORDENADAS:\").PopAll";
_cs.Append(BA.ObjectToCharSequence(" COORDENADAS:")).PopAll();
 //BA.debugLineNum = 446;BA.debugLine="cs.Append(CRLF & TAB)";
_cs.Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.TAB));
 //BA.debugLineNum = 448;BA.debugLine="cs.Append(\"→ X: \").PopAll";
_cs.Append(BA.ObjectToCharSequence("→ X: ")).PopAll();
 //BA.debugLineNum = 449;BA.debugLine="cs.Color(Colors.Blue).Append(dataMap.Get(\"x\")).";
_cs.Color(anywheresoftware.b4a.keywords.Common.Colors.Blue).Append(BA.ObjectToCharSequence(_datamap.Get((Object)("x")))).PopAll();
 //BA.debugLineNum = 450;BA.debugLine="cs.Append(CRLF & TAB)";
_cs.Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.TAB));
 //BA.debugLineNum = 452;BA.debugLine="cs.Append(\"→ Y: \").PopAll";
_cs.Append(BA.ObjectToCharSequence("→ Y: ")).PopAll();
 //BA.debugLineNum = 453;BA.debugLine="cs.Color(Colors.Blue).Append(dataMap.Get(\"y\")).";
_cs.Color(anywheresoftware.b4a.keywords.Common.Colors.Blue).Append(BA.ObjectToCharSequence(_datamap.Get((Object)("y")))).PopAll();
 //BA.debugLineNum = 454;BA.debugLine="cs.Append(CRLF)";
_cs.Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.CRLF));
 //BA.debugLineNum = 456;BA.debugLine="cs.Typeface(Typeface.MATERIALICONS).Append(Chr(";
_cs.Typeface(anywheresoftware.b4a.keywords.Common.Typeface.getMATERIALICONS()).Append(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.Chr(((int)0xe192)))).PopAll();
 //BA.debugLineNum = 457;BA.debugLine="cs.Append(\" FECHA HORA: \").PopAll";
_cs.Append(BA.ObjectToCharSequence(" FECHA HORA: ")).PopAll();
 //BA.debugLineNum = 458;BA.debugLine="cs.Color(Colors.Blue).Append(ConvertirTimestamp";
_cs.Color(anywheresoftware.b4a.keywords.Common.Colors.Blue).Append(BA.ObjectToCharSequence(_convertirtimestamp(BA.ObjectToLongNumber(_datamap.Get((Object)("timestamp")))))).PopAll();
 //BA.debugLineNum = 460;BA.debugLine="lblInfo.Text = cs";
mostCurrent._lblinfo.setText(BA.ObjectToCharSequence(_cs.getObject()));
 //BA.debugLineNum = 462;BA.debugLine="lblInfo.Height=150dip";
mostCurrent._lblinfo.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (150)));
 //BA.debugLineNum = 463;BA.debugLine="lblInfo.Visible=True";
mostCurrent._lblinfo.setVisible(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 465;BA.debugLine="lblInfo.Typeface=Typeface.DEFAULT";
mostCurrent._lblinfo.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 466;BA.debugLine="lblInfo.Text=\"ID Registro: \"&dataMap.Get(\"last_";
mostCurrent._lblinfo.setText(BA.ObjectToCharSequence("ID Registro: "+BA.ObjectToString(_datamap.Get((Object)("last_id")))+anywheresoftware.b4a.keywords.Common.CRLF+"Insertados: "+BA.ObjectToString(_datamap.Get((Object)("affectedRows")))));
 //BA.debugLineNum = 467;BA.debugLine="lblInfo.Height=50dip";
mostCurrent._lblinfo.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 468;BA.debugLine="lblInfo.Visible=True";
mostCurrent._lblinfo.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 } 
       catch (Exception e41) {
			processBA.setLastException(e41); //BA.debugLineNum = 472;BA.debugLine="ToastMessageShow(datos,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_datos),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 474;BA.debugLine="Log(dataMap)";
anywheresoftware.b4a.keywords.Common.LogImpl("31703991",BA.ObjectToString(_datamap),0);
 //BA.debugLineNum = 475;BA.debugLine="End Sub";
return "";
}
public static String  _connect_mqtt() throws Exception{
String _serveruri = "";
anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper _mo = null;
String _user = "";
String _pass = "";
 //BA.debugLineNum = 346;BA.debugLine="Sub connect_MQTT";
 //BA.debugLineNum = 347;BA.debugLine="If connectedMQTT Then Return";
if (_connectedmqtt) { 
if (true) return "";};
 //BA.debugLineNum = 348;BA.debugLine="Try";
try { //BA.debugLineNum = 349;BA.debugLine="If  Config1.Get(\"keySSL\") Then";
if (BA.ObjectToBoolean(_config1.Get((Object)("keySSL")))) { 
 //BA.debugLineNum = 350;BA.debugLine="Dim serverURI As String = \"ssl://\" & Config1.Ge";
_serveruri = "ssl://"+BA.ObjectToString(_config1.Get((Object)("keyHostMQTT")))+":"+BA.ObjectToString(_config1.Get((Object)("keyPortMQTT")));
 //BA.debugLineNum = 351;BA.debugLine="client.Initialize(\"clientMQTT\", serverURI, CLIE";
mostCurrent._client.Initialize(processBA,"clientMQTT",_serveruri,mostCurrent._client_id);
 }else {
 //BA.debugLineNum = 353;BA.debugLine="client.Initialize(\"clientMQTT\",\"tcp://\"&Config1";
mostCurrent._client.Initialize(processBA,"clientMQTT","tcp://"+BA.ObjectToString(_config1.Get((Object)("keyHostMQTT")))+":"+BA.ObjectToString(_config1.Get((Object)("keyPortMQTT"))),mostCurrent._client_id);
 };
 } 
       catch (Exception e10) {
			processBA.setLastException(e10); //BA.debugLineNum = 356;BA.debugLine="ToastMessageShow(LastException.Message,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage()),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 357;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 359;BA.debugLine="If Config1.Get(\"keyUser\")<>\"*\" Then";
if ((_config1.Get((Object)("keyUser"))).equals((Object)("*")) == false) { 
 //BA.debugLineNum = 360;BA.debugLine="Dim mo As MqttConnectOptions";
_mo = new anywheresoftware.b4j.objects.MqttAsyncClientWrapper.MqttConnectOptionsWrapper();
 //BA.debugLineNum = 361;BA.debugLine="Dim user As String=Config1.Get(\"keyUser\")";
_user = BA.ObjectToString(_config1.Get((Object)("keyUser")));
 //BA.debugLineNum = 362;BA.debugLine="Dim pass As String=Config1.Get(\"keyPass\")";
_pass = BA.ObjectToString(_config1.Get((Object)("keyPass")));
 //BA.debugLineNum = 363;BA.debugLine="mo.Initialize(user.trim, pass.trim)";
_mo.Initialize(_user.trim(),_pass.trim());
 };
 //BA.debugLineNum = 365;BA.debugLine="Try";
try { //BA.debugLineNum = 366;BA.debugLine="If Config1.Get(\"keyUser\")<>\"*\" Then";
if ((_config1.Get((Object)("keyUser"))).equals((Object)("*")) == false) { 
 //BA.debugLineNum = 367;BA.debugLine="client.Connect2(mo)";
mostCurrent._client.Connect2((org.eclipse.paho.client.mqttv3.MqttConnectOptions)(_mo.getObject()));
 }else {
 //BA.debugLineNum = 369;BA.debugLine="client.Connect";
mostCurrent._client.Connect();
 };
 //BA.debugLineNum = 371;BA.debugLine="If ErroresReconectando<1 Then";
if (_erroresreconectando<1) { 
 //BA.debugLineNum = 372;BA.debugLine="tmrMQTTReconnect.Enabled=True";
mostCurrent._tmrmqttreconnect.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 374;BA.debugLine="tmrMQTTReconnect.Enabled=False";
mostCurrent._tmrmqttreconnect.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 376;BA.debugLine="tmrAutoScan.Enabled=Config1.Get(\"keyAutoFind\")";
mostCurrent._tmrautoscan.setEnabled(BA.ObjectToBoolean(_config1.Get((Object)("keyAutoFind"))));
 } 
       catch (Exception e32) {
			processBA.setLastException(e32); //BA.debugLineNum = 378;BA.debugLine="ToastMessageShow(\"Error conectando \"&LastExcepti";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Error conectando "+anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage()),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 380;BA.debugLine="End Sub";
return "";
}
public static String  _convertirtimestamp(long _dt) throws Exception{
 //BA.debugLineNum = 413;BA.debugLine="Sub ConvertirTimestamp(dt As Long) As String";
 //BA.debugLineNum = 414;BA.debugLine="DateTime.DateFormat=\"dd/MM/yy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("dd/MM/yy");
 //BA.debugLineNum = 415;BA.debugLine="DateTime.TimeFormat=\"HH:ss:ss\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("HH:ss:ss");
 //BA.debugLineNum = 416;BA.debugLine="Return DateTime.Date(dt) & \" \" & DateTime.Time(dt";
if (true) return anywheresoftware.b4a.keywords.Common.DateTime.Date(_dt)+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(_dt);
 //BA.debugLineNum = 417;BA.debugLine="End Sub";
return "";
}
public static String  _converttojson(indoorgeolocation.aps.uvigo.main._tregistro _registro,anywheresoftware.b4a.objects.collections.List _wifiscans) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser.JSONGenerator _json = null;
anywheresoftware.b4a.objects.collections.Map _map = null;
 //BA.debugLineNum = 158;BA.debugLine="Sub ConvertToJSON(registro As TRegistro, wifiScans";
 //BA.debugLineNum = 159;BA.debugLine="Dim json As JSONGenerator";
_json = new anywheresoftware.b4a.objects.collections.JSONParser.JSONGenerator();
 //BA.debugLineNum = 160;BA.debugLine="Dim map As Map";
_map = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 161;BA.debugLine="map.Initialize";
_map.Initialize();
 //BA.debugLineNum = 162;BA.debugLine="map.Put(\"registro\", SaveTRegistroToMap(registro))";
_map.Put((Object)("registro"),(Object)(_savetregistrotomap(_registro).getObject()));
 //BA.debugLineNum = 163;BA.debugLine="map.Put(\"wifi_scans\", wifiScans)";
_map.Put((Object)("wifi_scans"),(Object)(_wifiscans.getObject()));
 //BA.debugLineNum = 164;BA.debugLine="json.Initialize(map)";
_json.Initialize(_map);
 //BA.debugLineNum = 165;BA.debugLine="Return json.ToString";
if (true) return _json.ToString();
 //BA.debugLineNum = 166;BA.debugLine="End Sub";
return "";
}
public static String  _getandroidid() throws Exception{
anywheresoftware.b4a.phone.Phone _p = null;
 //BA.debugLineNum = 117;BA.debugLine="Sub GetAndroidID As String";
 //BA.debugLineNum = 118;BA.debugLine="Dim p As Phone";
_p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 119;BA.debugLine="Return p.GetSettings(\"android_id\")";
if (true) return _p.GetSettings("android_id");
 //BA.debugLineNum = 120;BA.debugLine="End Sub";
return "";
}
public static String  _gettopic(boolean _location,boolean _result) throws Exception{
String _topic = "";
 //BA.debugLineNum = 313;BA.debugLine="Sub getTopic(location As Boolean,result As Boolean";
 //BA.debugLineNum = 314;BA.debugLine="Dim topic As String=Config1.Get(\"keyTopic\")";
_topic = BA.ObjectToString(_config1.Get((Object)("keyTopic")));
 //BA.debugLineNum = 315;BA.debugLine="If topic=Null Then";
if (_topic== null) { 
 //BA.debugLineNum = 316;BA.debugLine="topic=\"GX\"";
_topic = "GX";
 };
 //BA.debugLineNum = 318;BA.debugLine="If location Then";
if (_location) { 
 //BA.debugLineNum = 319;BA.debugLine="topic= \"indoor_geolocation/\"&topic&\"/location\"";
_topic = "indoor_geolocation/"+_topic+"/location";
 }else {
 //BA.debugLineNum = 321;BA.debugLine="topic= \"indoor_geolocation/\"&topic&\"/registro\"";
_topic = "indoor_geolocation/"+_topic+"/registro";
 };
 //BA.debugLineNum = 323;BA.debugLine="If result Then";
if (_result) { 
 //BA.debugLineNum = 324;BA.debugLine="topic= topic&\"/result\"";
_topic = _topic+"/result";
 }else {
 //BA.debugLineNum = 326;BA.debugLine="topic= topic&\"/data\"";
_topic = _topic+"/data";
 };
 //BA.debugLineNum = 328;BA.debugLine="Return topic";
if (true) return _topic;
 //BA.debugLineNum = 329;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 32;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 34;BA.debugLine="Private btnConfig As Button";
mostCurrent._btnconfig = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private prefdialog As PreferencesDialog";
mostCurrent._prefdialog = new indoorgeolocation.aps.uvigo.preferencesdialog();
 //BA.debugLineNum = 36;BA.debugLine="Private ConfigFile As String = \"config.map\"";
mostCurrent._configfile = "config.map";
 //BA.debugLineNum = 37;BA.debugLine="Dim conv As ByteConverter";
mostCurrent._conv = new anywheresoftware.b4a.agraham.byteconverter.ByteConverter();
 //BA.debugLineNum = 38;BA.debugLine="Dim client As MqttClient";
mostCurrent._client = new anywheresoftware.b4j.objects.MqttAsyncClientWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Dim connectedMQTT As Boolean";
_connectedmqtt = false;
 //BA.debugLineNum = 40;BA.debugLine="Private lblInfo As Label";
mostCurrent._lblinfo = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private tmrMQTTReconnect As Timer";
mostCurrent._tmrmqttreconnect = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 42;BA.debugLine="Private tmrAutoScan As Timer";
mostCurrent._tmrautoscan = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 43;BA.debugLine="Private btnScan As Button";
mostCurrent._btnscan = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private lblSend As Label";
mostCurrent._lblsend = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private lblScan As Label";
mostCurrent._lblscan = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private lblDisconnect As Label";
mostCurrent._lbldisconnect = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private lblConnect As Label";
mostCurrent._lblconnect = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private scanningWifi As Boolean";
_scanningwifi = false;
 //BA.debugLineNum = 49;BA.debugLine="Private counter As Int";
_counter = 0;
 //BA.debugLineNum = 50;BA.debugLine="Private counterSec As Int";
_countersec = 0;
 //BA.debugLineNum = 51;BA.debugLine="Private lblIconWifi As ImageView";
mostCurrent._lbliconwifi = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private CLIENT_ID As String";
mostCurrent._client_id = "";
 //BA.debugLineNum = 53;BA.debugLine="End Sub";
return "";
}
public static boolean  _loadconfig() throws Exception{
 //BA.debugLineNum = 479;BA.debugLine="private Sub LoadConfig As Boolean";
 //BA.debugLineNum = 480;BA.debugLine="Config1.Initialize";
_config1.Initialize();
 //BA.debugLineNum = 481;BA.debugLine="xui.SetDataFolder (\"preferences\")";
_xui.SetDataFolder("preferences");
 //BA.debugLineNum = 482;BA.debugLine="prefdialog.Initialize(Activity, \"Configuración\",";
mostCurrent._prefdialog._initialize /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._activity.getObject())),(Object)("Configuración"),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (300)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (500)));
 //BA.debugLineNum = 483;BA.debugLine="prefdialog.LoadFromJson(File.ReadString(File.DirA";
mostCurrent._prefdialog._loadfromjson /*String*/ (anywheresoftware.b4a.keywords.Common.File.ReadString(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"config.json"));
 //BA.debugLineNum = 485;BA.debugLine="prefdialog.SetEventsListener(Me, \"PrefDialog\")";
mostCurrent._prefdialog._seteventslistener /*String*/ (main.getObject(),"PrefDialog");
 //BA.debugLineNum = 486;BA.debugLine="Return LoadSavedData";
if (true) return _loadsaveddata();
 //BA.debugLineNum = 487;BA.debugLine="End Sub";
return false;
}
public static boolean  _loadsaveddata() throws Exception{
String _jsonstring = "";
anywheresoftware.b4a.objects.collections.JSONParser _jsonparser = null;
anywheresoftware.b4a.objects.collections.Map _m = null;
 //BA.debugLineNum = 498;BA.debugLine="Private Sub LoadSavedData As Boolean";
 //BA.debugLineNum = 499;BA.debugLine="Try";
try { //BA.debugLineNum = 500;BA.debugLine="If File.Exists(xui.DefaultFolder, ConfigFile) Th";
if (anywheresoftware.b4a.keywords.Common.File.Exists(_xui.getDefaultFolder(),mostCurrent._configfile)) { 
 //BA.debugLineNum = 501;BA.debugLine="Dim jsonString As String = File.ReadString(xui.";
_jsonstring = anywheresoftware.b4a.keywords.Common.File.ReadString(_xui.getDefaultFolder(),mostCurrent._configfile);
 //BA.debugLineNum = 502;BA.debugLine="Dim JSONParser As JSONParser";
_jsonparser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 503;BA.debugLine="JSONParser.Initialize(jsonString)";
_jsonparser.Initialize(_jsonstring);
 //BA.debugLineNum = 504;BA.debugLine="Dim m As Map = JSONParser.NextObject";
_m = new anywheresoftware.b4a.objects.collections.Map();
_m = _jsonparser.NextObject();
 //BA.debugLineNum = 505;BA.debugLine="Config1=m";
_config1 = _m;
 //BA.debugLineNum = 506;BA.debugLine="SaveConfig(Config1)";
_saveconfig(_config1);
 //BA.debugLineNum = 507;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 509;BA.debugLine="Config1.Put(\"keyHostMQTT\",    \"test.mosquitto.o";
_config1.Put((Object)("keyHostMQTT"),(Object)("test.mosquitto.org"));
 //BA.debugLineNum = 510;BA.debugLine="Config1.Put(\"keyPortMQTT\",    1883)";
_config1.Put((Object)("keyPortMQTT"),(Object)(1883));
 //BA.debugLineNum = 511;BA.debugLine="Config1.Put(\"keyUser\",        \"*\")";
_config1.Put((Object)("keyUser"),(Object)("*"));
 //BA.debugLineNum = 512;BA.debugLine="Config1.Put(\"keyPass\",        \"\")";
_config1.Put((Object)("keyPass"),(Object)(""));
 //BA.debugLineNum = 513;BA.debugLine="Config1.Put(\"keySSL\",        False)";
_config1.Put((Object)("keySSL"),(Object)(anywheresoftware.b4a.keywords.Common.False));
 //BA.debugLineNum = 514;BA.debugLine="Config1.Put(\"keyTopic\",       \"GX\")";
_config1.Put((Object)("keyTopic"),(Object)("GX"));
 //BA.debugLineNum = 515;BA.debugLine="Config1.Put(\"keyAutoFind\",    False)";
_config1.Put((Object)("keyAutoFind"),(Object)(anywheresoftware.b4a.keywords.Common.False));
 //BA.debugLineNum = 517;BA.debugLine="SaveConfig(Config1)";
_saveconfig(_config1);
 //BA.debugLineNum = 518;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 } 
       catch (Exception e22) {
			processBA.setLastException(e22); //BA.debugLineNum = 521;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("31900567",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 522;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 524;BA.debugLine="End Sub";
return false;
}
public static boolean  _prefdialog_isvalid(anywheresoftware.b4a.objects.collections.Map _tempdata) throws Exception{
 //BA.debugLineNum = 489;BA.debugLine="Private Sub PrefDialog_IsValid (TempData As Map) A";
 //BA.debugLineNum = 495;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 496;BA.debugLine="End Sub";
return false;
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        b4a.example.dateutils._process_globals();
main._process_globals();
starter._process_globals();
xuiviewsutils._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 18;BA.debugLine="Dim WifiObject As MLwifi";
_wifiobject = new com.airlinemates.mlwifi.MLwifi();
 //BA.debugLineNum = 19;BA.debugLine="Private wifiScan As MLScan";
_wifiscan = new com.airlinemates.mlwifi.MLwifi.MLWifiScanner();
 //BA.debugLineNum = 20;BA.debugLine="Type TRegistro(fabricante As String, modelo As St";
;
 //BA.debugLineNum = 21;BA.debugLine="Type TWifiScan(SSID As String, BSSID As String, s";
;
 //BA.debugLineNum = 22;BA.debugLine="Dim pmanager As PermissionsManager";
_pmanager = new igolub.permissionmanager.b4a.PermissionsManager();
 //BA.debugLineNum = 23;BA.debugLine="Dim phone As Phone";
_phone = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 24;BA.debugLine="Private Config1 As Map";
_config1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 25;BA.debugLine="Private DataPoint As Map";
_datapoint = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 26;BA.debugLine="Private xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 27;BA.debugLine="Private androidID As String";
_androidid = "";
 //BA.debugLineNum = 28;BA.debugLine="Private tickCountExit As Long";
_tickcountexit = 0L;
 //BA.debugLineNum = 29;BA.debugLine="Private ErroresReconectando As Int=0";
_erroresreconectando = (int) (0);
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
public static String  _saveconfig(anywheresoftware.b4a.objects.collections.Map _config) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser.JSONGenerator _jsongenerator = null;
String _jsonstring = "";
 //BA.debugLineNum = 526;BA.debugLine="Private Sub SaveConfig (Config As Map)";
 //BA.debugLineNum = 527;BA.debugLine="Dim JSONGenerator As JSONGenerator";
_jsongenerator = new anywheresoftware.b4a.objects.collections.JSONParser.JSONGenerator();
 //BA.debugLineNum = 528;BA.debugLine="JSONGenerator.Initialize(Config)";
_jsongenerator.Initialize(_config);
 //BA.debugLineNum = 529;BA.debugLine="Dim jsonString As String = JSONGenerator.ToString";
_jsonstring = _jsongenerator.ToString();
 //BA.debugLineNum = 530;BA.debugLine="File.WriteString(xui.DefaultFolder, ConfigFile, j";
anywheresoftware.b4a.keywords.Common.File.WriteString(_xui.getDefaultFolder(),mostCurrent._configfile,_jsonstring);
 //BA.debugLineNum = 531;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.Map  _savetregistrotomap(indoorgeolocation.aps.uvigo.main._tregistro _r) throws Exception{
anywheresoftware.b4a.objects.collections.Map _m = null;
 //BA.debugLineNum = 122;BA.debugLine="Sub SaveTRegistroToMap(r As TRegistro) As Map";
 //BA.debugLineNum = 123;BA.debugLine="Dim m As Map";
_m = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 124;BA.debugLine="m.Initialize";
_m.Initialize();
 //BA.debugLineNum = 125;BA.debugLine="m.Put(\"fabricante\", r.fabricante)";
_m.Put((Object)("fabricante"),(Object)(_r.fabricante /*String*/ ));
 //BA.debugLineNum = 126;BA.debugLine="m.Put(\"modelo\", r.modelo)";
_m.Put((Object)("modelo"),(Object)(_r.modelo /*String*/ ));
 //BA.debugLineNum = 127;BA.debugLine="m.Put(\"producto\", r.producto)";
_m.Put((Object)("producto"),(Object)(_r.producto /*String*/ ));
 //BA.debugLineNum = 128;BA.debugLine="m.Put(\"sdk\", r.sdk)";
_m.Put((Object)("sdk"),(Object)(_r.sdk /*String*/ ));
 //BA.debugLineNum = 129;BA.debugLine="m.Put(\"androidID\", androidID)";
_m.Put((Object)("androidID"),(Object)(_androidid));
 //BA.debugLineNum = 130;BA.debugLine="m.Put(\"fecha_hora\", r.fecha_hora)";
_m.Put((Object)("fecha_hora"),(Object)(_r.fecha_hora /*long*/ ));
 //BA.debugLineNum = 131;BA.debugLine="m.Put(\"nivel\", r.nivel)";
_m.Put((Object)("nivel"),(Object)(_r.nivel /*String*/ ));
 //BA.debugLineNum = 132;BA.debugLine="m.Put(\"punto\", r.punto)";
_m.Put((Object)("punto"),(Object)(_r.punto /*int*/ ));
 //BA.debugLineNum = 133;BA.debugLine="m.Put(\"counter\", r.counter)";
_m.Put((Object)("counter"),(Object)(_r.counter /*int*/ ));
 //BA.debugLineNum = 134;BA.debugLine="Return m";
if (true) return _m;
 //BA.debugLineNum = 135;BA.debugLine="End Sub";
return null;
}
public static void  _scanap() throws Exception{
ResumableSub_scanAP rsub = new ResumableSub_scanAP(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_scanAP extends BA.ResumableSub {
public ResumableSub_scanAP(indoorgeolocation.aps.uvigo.main parent) {
this.parent = parent;
}
indoorgeolocation.aps.uvigo.main parent;
int _result = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 239;BA.debugLine="If Config1.Get(\"keyAutoFind\")=False Then";
if (true) break;

case 1:
//if
this.state = 4;
if ((parent._config1.Get((Object)("keyAutoFind"))).equals((Object)(anywheresoftware.b4a.keywords.Common.False))) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 240;BA.debugLine="lblInfo.Visible=False";
parent.mostCurrent._lblinfo.setVisible(anywheresoftware.b4a.keywords.Common.False);
 if (true) break;
;
 //BA.debugLineNum = 242;BA.debugLine="If Not(client.Connected ) Then";

case 4:
//if
this.state = 29;
if (anywheresoftware.b4a.keywords.Common.Not(parent.mostCurrent._client.getConnected())) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 29;
 //BA.debugLineNum = 243;BA.debugLine="ToastMessageShow(\"No conectado al servidor MQTT.";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No conectado al servidor MQTT."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 245;BA.debugLine="If Not (WifiObject.isWifiEnabled) Then";
if (true) break;

case 9:
//if
this.state = 28;
if (anywheresoftware.b4a.keywords.Common.Not(parent._wifiobject.isWifiEnabled())) { 
this.state = 11;
}else {
this.state = 13;
}if (true) break;

case 11:
//C
this.state = 28;
 //BA.debugLineNum = 246;BA.debugLine="ToastMessageShow(\"La wifi no está activada.\",Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("La wifi no está activada."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 248;BA.debugLine="If Config1.Get(\"keyAutoFind\") Then";
if (true) break;

case 14:
//if
this.state = 27;
if (BA.ObjectToBoolean(parent._config1.Get((Object)("keyAutoFind")))) { 
this.state = 16;
}else {
this.state = 18;
}if (true) break;

case 16:
//C
this.state = 27;
 //BA.debugLineNum = 249;BA.debugLine="scanningWifi=True";
parent._scanningwifi = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 250;BA.debugLine="wifiScan.startScan(\"wifi\",False)";
parent._wifiscan.startScan(processBA,"wifi",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 252;BA.debugLine="prefdialog.Initialize(Activity, \"Datos punto\",";
parent.mostCurrent._prefdialog._initialize /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject())),(Object)("Datos punto"),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (300)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (500)));
 //BA.debugLineNum = 253;BA.debugLine="prefdialog.LoadFromJson(File.ReadString(File.D";
parent.mostCurrent._prefdialog._loadfromjson /*String*/ (anywheresoftware.b4a.keywords.Common.File.ReadString(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"paramRegister.json"));
 //BA.debugLineNum = 254;BA.debugLine="If Not (DataPoint.IsInitialized) Then";
if (true) break;

case 19:
//if
this.state = 22;
if (anywheresoftware.b4a.keywords.Common.Not(parent._datapoint.IsInitialized())) { 
this.state = 21;
}if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 255;BA.debugLine="DataPoint.Initialize";
parent._datapoint.Initialize();
 //BA.debugLineNum = 256;BA.debugLine="DataPoint.Put(\"keyLevel\",\"PB\")";
parent._datapoint.Put((Object)("keyLevel"),(Object)("PB"));
 //BA.debugLineNum = 257;BA.debugLine="DataPoint.Put(\"keyPoint\",1)";
parent._datapoint.Put((Object)("keyPoint"),(Object)(1));
 if (true) break;

case 22:
//C
this.state = 23;
;
 //BA.debugLineNum = 259;BA.debugLine="Wait For (prefdialog.ShowDialog(DataPoint, \"OK";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, parent.mostCurrent._prefdialog._showdialog /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ (parent._datapoint,(Object)("OK"),(Object)("CANCELAR")));
this.state = 30;
return;
case 30:
//C
this.state = 23;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 260;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 23:
//if
this.state = 26;
if (_result==parent._xui.DialogResponse_Positive) { 
this.state = 25;
}if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 261;BA.debugLine="ProgressDialogShow(\"Escaneando...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Escaneando..."));
 //BA.debugLineNum = 262;BA.debugLine="wifiScan.startScan(\"wifi\",False)";
parent._wifiscan.startScan(processBA,"wifi",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 26:
//C
this.state = 27;
;
 if (true) break;

case 27:
//C
this.state = 28;
;
 if (true) break;

case 28:
//C
this.state = 29;
;
 if (true) break;

case 29:
//C
this.state = -1;
;
 //BA.debugLineNum = 269;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _setstatemqtt() throws Exception{
 //BA.debugLineNum = 99;BA.debugLine="Sub setStateMQTT";
 //BA.debugLineNum = 100;BA.debugLine="If connectedMQTT Then";
if (_connectedmqtt) { 
 //BA.debugLineNum = 101;BA.debugLine="lblConnect.Text=\"\"";
mostCurrent._lblconnect.setText(BA.ObjectToCharSequence(""));
 }else {
 //BA.debugLineNum = 103;BA.debugLine="lblConnect.Text=\"\"";
mostCurrent._lblconnect.setText(BA.ObjectToCharSequence(""));
 };
 //BA.debugLineNum = 107;BA.debugLine="End Sub";
return "";
}
public static String  _setstatescan() throws Exception{
 //BA.debugLineNum = 109;BA.debugLine="Sub setStateScan";
 //BA.debugLineNum = 110;BA.debugLine="If  Not(Config1.IsInitialized) Or Not(Config1.Get";
if (anywheresoftware.b4a.keywords.Common.Not(_config1.IsInitialized()) || anywheresoftware.b4a.keywords.Common.Not(BA.ObjectToBoolean(_config1.Get((Object)("keyAutoFind"))))) { 
 //BA.debugLineNum = 111;BA.debugLine="lblScan.Visible=False";
mostCurrent._lblscan.setVisible(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 113;BA.debugLine="lblScan.Visible=Not(lblScan.Visible)";
mostCurrent._lblscan.setVisible(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._lblscan.getVisible()));
 };
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
return "";
}
public static String  _tmrautoscan_tick() throws Exception{
 //BA.debugLineNum = 287;BA.debugLine="Sub tmrAutoScan_Tick";
 //BA.debugLineNum = 289;BA.debugLine="If Not(scanningWifi) Then";
if (anywheresoftware.b4a.keywords.Common.Not(_scanningwifi)) { 
 //BA.debugLineNum = 290;BA.debugLine="scanAP";
_scanap();
 }else {
 //BA.debugLineNum = 292;BA.debugLine="counterSec=counterSec+1";
_countersec = (int) (_countersec+1);
 //BA.debugLineNum = 293;BA.debugLine="If counterSec>10 Then";
if (_countersec>10) { 
 //BA.debugLineNum = 294;BA.debugLine="counterSec=0";
_countersec = (int) (0);
 //BA.debugLineNum = 295;BA.debugLine="lblSend.Text=(counter*6)&\" scan/min\"";
mostCurrent._lblsend.setText(BA.ObjectToCharSequence(BA.NumberToString((_counter*6))+" scan/min"));
 //BA.debugLineNum = 296;BA.debugLine="counter=0";
_counter = (int) (0);
 //BA.debugLineNum = 297;BA.debugLine="lblSend.Visible=True";
mostCurrent._lblsend.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 };
 //BA.debugLineNum = 300;BA.debugLine="setStateScan";
_setstatescan();
 //BA.debugLineNum = 301;BA.debugLine="End Sub";
return "";
}
public static String  _tmrmqttreconnect_tick() throws Exception{
 //BA.debugLineNum = 303;BA.debugLine="Sub tmrMQTTReconnect_Tick";
 //BA.debugLineNum = 304;BA.debugLine="If ErroresReconectando>=3 Then";
if (_erroresreconectando>=3) { 
 //BA.debugLineNum = 305;BA.debugLine="tmrMQTTReconnect.Enabled=False";
mostCurrent._tmrmqttreconnect.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 307;BA.debugLine="If client.Connected= False Then";
if (mostCurrent._client.getConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 308;BA.debugLine="connect_MQTT";
_connect_mqtt();
 };
 //BA.debugLineNum = 310;BA.debugLine="End Sub";
return "";
}
public static String  _topiclocation() throws Exception{
 //BA.debugLineNum = 338;BA.debugLine="Sub topicLocation As String";
 //BA.debugLineNum = 339;BA.debugLine="Return getTopic(True,False)";
if (true) return _gettopic(anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 340;BA.debugLine="End Sub";
return "";
}
public static String  _topiclocationresult() throws Exception{
 //BA.debugLineNum = 342;BA.debugLine="Sub topicLocationResult As String";
 //BA.debugLineNum = 343;BA.debugLine="Return getTopic(True,True)";
if (true) return _gettopic(anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 344;BA.debugLine="End Sub";
return "";
}
public static String  _topicregistro() throws Exception{
 //BA.debugLineNum = 331;BA.debugLine="Sub topicRegistro As String";
 //BA.debugLineNum = 332;BA.debugLine="Return getTopic(False,False)";
if (true) return _gettopic(anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 333;BA.debugLine="End Sub";
return "";
}
public static String  _topicregistroresult() throws Exception{
 //BA.debugLineNum = 334;BA.debugLine="Sub topicRegistroResult As String";
 //BA.debugLineNum = 335;BA.debugLine="Return getTopic(False,True)";
if (true) return _gettopic(anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 336;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.Map  _twifiscantomap(indoorgeolocation.aps.uvigo.main._twifiscan _awifiscan) throws Exception{
anywheresoftware.b4a.objects.collections.Map _m = null;
 //BA.debugLineNum = 137;BA.debugLine="Sub TWifiScanToMap(awifiScan As TWifiScan) As Map";
 //BA.debugLineNum = 138;BA.debugLine="Dim m As Map";
_m = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 139;BA.debugLine="m.Initialize";
_m.Initialize();
 //BA.debugLineNum = 140;BA.debugLine="m.Put(\"SSID\", awifiScan.SSID)";
_m.Put((Object)("SSID"),(Object)(_awifiscan.SSID /*String*/ ));
 //BA.debugLineNum = 141;BA.debugLine="m.Put(\"BSSID\", awifiScan.BSSID)";
_m.Put((Object)("BSSID"),(Object)(_awifiscan.BSSID /*String*/ ));
 //BA.debugLineNum = 142;BA.debugLine="m.Put(\"seguridad\", awifiScan.seguridad)";
_m.Put((Object)("seguridad"),(Object)(_awifiscan.seguridad /*String*/ ));
 //BA.debugLineNum = 143;BA.debugLine="m.Put(\"potencia\", awifiScan.potencia)";
_m.Put((Object)("potencia"),(Object)(_awifiscan.potencia /*int*/ ));
 //BA.debugLineNum = 144;BA.debugLine="m.Put(\"canal\", awifiScan.canal)";
_m.Put((Object)("canal"),(Object)(_awifiscan.canal /*int*/ ));
 //BA.debugLineNum = 145;BA.debugLine="m.Put(\"distancia\", awifiScan.distancia)";
_m.Put((Object)("distancia"),(Object)(_awifiscan.distancia /*double*/ ));
 //BA.debugLineNum = 146;BA.debugLine="m.Put(\"data_0\", awifiScan.data_0)";
_m.Put((Object)("data_0"),(Object)(_awifiscan.data_0 /*int*/ ));
 //BA.debugLineNum = 147;BA.debugLine="m.Put(\"data_1\", awifiScan.data_1)";
_m.Put((Object)("data_1"),(Object)(_awifiscan.data_1 /*int*/ ));
 //BA.debugLineNum = 148;BA.debugLine="m.Put(\"data_2\", awifiScan.data_2)";
_m.Put((Object)("data_2"),(Object)(_awifiscan.data_2 /*int*/ ));
 //BA.debugLineNum = 149;BA.debugLine="m.Put(\"data_3\", awifiScan.data_3)";
_m.Put((Object)("data_3"),(Object)(_awifiscan.data_3 /*int*/ ));
 //BA.debugLineNum = 150;BA.debugLine="m.Put(\"data_4\", awifiScan.data_4)";
_m.Put((Object)("data_4"),(Object)(_awifiscan.data_4 /*int*/ ));
 //BA.debugLineNum = 151;BA.debugLine="m.Put(\"data_5\", awifiScan.data_5)";
_m.Put((Object)("data_5"),(Object)(_awifiscan.data_5 /*int*/ ));
 //BA.debugLineNum = 152;BA.debugLine="m.Put(\"data_6\", awifiScan.data_6)";
_m.Put((Object)("data_6"),(Object)(_awifiscan.data_6 /*int*/ ));
 //BA.debugLineNum = 153;BA.debugLine="m.Put(\"data_7\", awifiScan.data_7)";
_m.Put((Object)("data_7"),(Object)(_awifiscan.data_7 /*int*/ ));
 //BA.debugLineNum = 154;BA.debugLine="m.Put(\"data_8\", awifiScan.data_8)";
_m.Put((Object)("data_8"),(Object)(_awifiscan.data_8 /*int*/ ));
 //BA.debugLineNum = 155;BA.debugLine="m.Put(\"data_9\", awifiScan.data_9)";
_m.Put((Object)("data_9"),(Object)(_awifiscan.data_9 /*int*/ ));
 //BA.debugLineNum = 156;BA.debugLine="Return m";
if (true) return _m;
 //BA.debugLineNum = 157;BA.debugLine="End Sub";
return null;
}
public static String  _wifi_scandone(String[] _results,int _count) throws Exception{
indoorgeolocation.aps.uvigo.main._tregistro _r = null;
indoorgeolocation.aps.uvigo.main._twifiscan _record = null;
anywheresoftware.b4a.objects.collections.List _listforlist = null;
String[] _returnedentry = null;
int _i = 0;
String _data_json = "";
 //BA.debugLineNum = 168;BA.debugLine="Sub wifi_ScanDone(Results() As String, Count As In";
 //BA.debugLineNum = 169;BA.debugLine="counter=counter+1";
_counter = (int) (_counter+1);
 //BA.debugLineNum = 170;BA.debugLine="lblSend.Text=counter";
mostCurrent._lblsend.setText(BA.ObjectToCharSequence(_counter));
 //BA.debugLineNum = 171;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 172;BA.debugLine="scanningWifi=False";
_scanningwifi = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 173;BA.debugLine="If Not(connectedMQTT) Then";
if (anywheresoftware.b4a.keywords.Common.Not(_connectedmqtt)) { 
 //BA.debugLineNum = 174;BA.debugLine="ToastMessageShow(\"No estas conectado al servidor";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No estas conectado al servidor MQTT."),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 175;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 177;BA.debugLine="If Count=0 Then";
if (_count==0) { 
 //BA.debugLineNum = 178;BA.debugLine="ToastMessageShow(\"No se han detectado APs, intén";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No se han detectado APs, inténtalo de nuevo."),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 179;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 181;BA.debugLine="Dim r As TRegistro";
_r = new indoorgeolocation.aps.uvigo.main._tregistro();
 //BA.debugLineNum = 182;BA.debugLine="r.Initialize";
_r.Initialize();
 //BA.debugLineNum = 183;BA.debugLine="r.fabricante = phone.Manufacturer";
_r.fabricante /*String*/  = _phone.getManufacturer();
 //BA.debugLineNum = 184;BA.debugLine="r.modelo = phone.Model";
_r.modelo /*String*/  = _phone.getModel();
 //BA.debugLineNum = 185;BA.debugLine="r.producto=phone.Product";
_r.producto /*String*/  = _phone.getProduct();
 //BA.debugLineNum = 186;BA.debugLine="r.sdk=phone.SdkVersion";
_r.sdk /*String*/  = BA.NumberToString(_phone.getSdkVersion());
 //BA.debugLineNum = 187;BA.debugLine="r.androidID=androidID";
_r.androidID /*String*/  = _androidid;
 //BA.debugLineNum = 188;BA.debugLine="r.fecha_hora = DateTime.Now ' Almacena la fecha y";
_r.fecha_hora /*long*/  = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 189;BA.debugLine="If  Config1.Get(\"keyAutoFind\")=False Then";
if ((_config1.Get((Object)("keyAutoFind"))).equals((Object)(anywheresoftware.b4a.keywords.Common.False))) { 
 //BA.debugLineNum = 190;BA.debugLine="r.nivel=DataPoint.Get(\"keyLevel\")";
_r.nivel /*String*/  = BA.ObjectToString(_datapoint.Get((Object)("keyLevel")));
 //BA.debugLineNum = 191;BA.debugLine="r.punto=DataPoint.Get(\"keyPoint\")";
_r.punto /*int*/  = (int)(BA.ObjectToNumber(_datapoint.Get((Object)("keyPoint"))));
 };
 //BA.debugLineNum = 193;BA.debugLine="r.counter=counter";
_r.counter /*int*/  = _counter;
 //BA.debugLineNum = 194;BA.debugLine="Dim record As TWifiScan";
_record = new indoorgeolocation.aps.uvigo.main._twifiscan();
 //BA.debugLineNum = 195;BA.debugLine="record.initialize";
_record.Initialize();
 //BA.debugLineNum = 196;BA.debugLine="Dim ListForList As List";
_listforlist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 197;BA.debugLine="ListForList.Initialize()";
_listforlist.Initialize();
 //BA.debugLineNum = 198;BA.debugLine="Dim returnedEntry() As String";
_returnedentry = new String[(int) (0)];
java.util.Arrays.fill(_returnedentry,"");
 //BA.debugLineNum = 199;BA.debugLine="For i = 0 To Results.Length-1";
{
final int step31 = 1;
final int limit31 = (int) (_results.length-1);
_i = (int) (0) ;
for (;_i <= limit31 ;_i = _i + step31 ) {
 //BA.debugLineNum = 200;BA.debugLine="returnedEntry = Regex.Split(\",\",Results(i))";
_returnedentry = anywheresoftware.b4a.keywords.Common.Regex.Split(",",_results[_i]);
 //BA.debugLineNum = 201;BA.debugLine="record.ssid = returnedEntry(0)";
_record.SSID /*String*/  = _returnedentry[(int) (0)];
 //BA.debugLineNum = 202;BA.debugLine="record.seguridad = returnedEntry(1)";
_record.seguridad /*String*/  = _returnedentry[(int) (1)];
 //BA.debugLineNum = 203;BA.debugLine="record.potencia = returnedEntry(2)";
_record.potencia /*int*/  = (int)(Double.parseDouble(_returnedentry[(int) (2)]));
 //BA.debugLineNum = 204;BA.debugLine="record.bssid = returnedEntry(4)";
_record.BSSID /*String*/  = _returnedentry[(int) (4)];
 //BA.debugLineNum = 205;BA.debugLine="record.canal = returnedEntry(10)";
_record.canal /*int*/  = (int)(Double.parseDouble(_returnedentry[(int) (10)]));
 //BA.debugLineNum = 206;BA.debugLine="record.distancia=returnedEntry(12)";
_record.distancia /*double*/  = (double)(Double.parseDouble(_returnedentry[(int) (12)]));
 //BA.debugLineNum = 208;BA.debugLine="record.data_1=returnedEntry(5)";
_record.data_1 /*int*/  = (int)(Double.parseDouble(_returnedentry[(int) (5)]));
 //BA.debugLineNum = 209;BA.debugLine="record.data_2=returnedEntry(6)";
_record.data_2 /*int*/  = (int)(Double.parseDouble(_returnedentry[(int) (6)]));
 //BA.debugLineNum = 210;BA.debugLine="record.data_3=returnedEntry(7)";
_record.data_3 /*int*/  = (int)(Double.parseDouble(_returnedentry[(int) (7)]));
 //BA.debugLineNum = 211;BA.debugLine="record.data_4=returnedEntry(8)";
_record.data_4 /*int*/  = (int)(Double.parseDouble(_returnedentry[(int) (8)]));
 //BA.debugLineNum = 212;BA.debugLine="record.data_5=returnedEntry(9)";
_record.data_5 /*int*/  = (int)(Double.parseDouble(_returnedentry[(int) (9)]));
 //BA.debugLineNum = 213;BA.debugLine="record.data_6=returnedEntry(11)";
_record.data_6 /*int*/  = (int)(Double.parseDouble(_returnedentry[(int) (11)]));
 //BA.debugLineNum = 214;BA.debugLine="ListForList.Add(TWifiScanToMap(record))";
_listforlist.Add((Object)(_twifiscantomap(_record).getObject()));
 }
};
 //BA.debugLineNum = 217;BA.debugLine="If client.Connected Then";
if (mostCurrent._client.getConnected()) { 
 //BA.debugLineNum = 218;BA.debugLine="Dim data_json As String=ConvertToJSON(r,ListForL";
_data_json = _converttojson(_r,_listforlist);
 //BA.debugLineNum = 219;BA.debugLine="If  Config1.Get(\"keyAutoFind\")=False Then";
if ((_config1.Get((Object)("keyAutoFind"))).equals((Object)(anywheresoftware.b4a.keywords.Common.False))) { 
 //BA.debugLineNum = 220;BA.debugLine="client.Publish2(topicRegistro,data_json.GetByte";
mostCurrent._client.Publish2(_topicregistro(),_data_json.getBytes("UTF8"),(int) (0),anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 222;BA.debugLine="client.Publish2(topicLocation,data_json.GetByte";
mostCurrent._client.Publish2(_topiclocation(),_data_json.getBytes("UTF8"),(int) (0),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 224;BA.debugLine="If  Config1.Get(\"keyAutoFind\")=False Then";
if ((_config1.Get((Object)("keyAutoFind"))).equals((Object)(anywheresoftware.b4a.keywords.Common.False))) { 
 //BA.debugLineNum = 225;BA.debugLine="ToastMessageShow(\"Detectadas \"&Results.Length&\"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Detectadas "+BA.NumberToString(_results.length)+" APs"+anywheresoftware.b4a.keywords.Common.CRLF+"Enviados datos..."),anywheresoftware.b4a.keywords.Common.True);
 };
 }else {
 //BA.debugLineNum = 228;BA.debugLine="Log(\"MQTT no conectado\")";
anywheresoftware.b4a.keywords.Common.LogImpl("3786492","MQTT no conectado",0);
 //BA.debugLineNum = 229;BA.debugLine="ToastMessageShow(\"MQTT no conectado\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("MQTT no conectado"),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 232;BA.debugLine="End Sub";
return "";
}
}
