package indoorgeolocation.aps.uvigo.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_lytmain{

public static void LS_general(anywheresoftware.b4a.BA ba, android.view.View parent, anywheresoftware.b4a.keywords.LayoutValues lv, java.util.Map props,
java.util.Map<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) throws Exception {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 2;BA.debugLine="AutoScaleAll"[LytMain/General script]
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
//BA.debugLineNum = 3;BA.debugLine="lblIconWifi.Left=(100%x-lblIconWifi.Width)/2"[LytMain/General script]
views.get("lbliconwifi").vw.setLeft((int)(((100d / 100 * width)-(views.get("lbliconwifi").vw.getWidth()))/2d));
//BA.debugLineNum = 4;BA.debugLine="pnlQRImage.Left=(100%x-pnlQRImage.Width)/2"[LytMain/General script]
views.get("pnlqrimage").vw.setLeft((int)(((100d / 100 * width)-(views.get("pnlqrimage").vw.getWidth()))/2d));
//BA.debugLineNum = 5;BA.debugLine="lblInfo.Left=(100%x-lblInfo.Width)/2"[LytMain/General script]
views.get("lblinfo").vw.setLeft((int)(((100d / 100 * width)-(views.get("lblinfo").vw.getWidth()))/2d));
//BA.debugLineNum = 6;BA.debugLine="ImageView1.Left=(100%x-ImageView1.Width)/2"[LytMain/General script]
views.get("imageview1").vw.setLeft((int)(((100d / 100 * width)-(views.get("imageview1").vw.getWidth()))/2d));
//BA.debugLineNum = 7;BA.debugLine="lblInfoCaption.Left=(100%x-lblInfoCaption.Width)/2"[LytMain/General script]
views.get("lblinfocaption").vw.setLeft((int)(((100d / 100 * width)-(views.get("lblinfocaption").vw.getWidth()))/2d));
//BA.debugLineNum = 8;BA.debugLine="btnScan.Left=(100%x-btnScan.Width-btnConfig.Width) /3"[LytMain/General script]
views.get("btnscan").vw.setLeft((int)(((100d / 100 * width)-(views.get("btnscan").vw.getWidth())-(views.get("btnconfig").vw.getWidth()))/3d));
//BA.debugLineNum = 9;BA.debugLine="btnConfig.Left=100%x-btnConfig.Width-(100%x-btnScan.Width-btnConfig.Width) /3"[LytMain/General script]
views.get("btnconfig").vw.setLeft((int)((100d / 100 * width)-(views.get("btnconfig").vw.getWidth())-((100d / 100 * width)-(views.get("btnscan").vw.getWidth())-(views.get("btnconfig").vw.getWidth()))/3d));
//BA.debugLineNum = 10;BA.debugLine="btnScan.Top=100%y-btnScan.Height-20dip"[LytMain/General script]
views.get("btnscan").vw.setTop((int)((100d / 100 * height)-(views.get("btnscan").vw.getHeight())-(20d * scale)));
//BA.debugLineNum = 11;BA.debugLine="btnConfig.Top=100%y-btnConfig.Height-20dip"[LytMain/General script]
views.get("btnconfig").vw.setTop((int)((100d / 100 * height)-(views.get("btnconfig").vw.getHeight())-(20d * scale)));

}
}