package com.shieldsurf.browser;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.*;
import android.widget.*;
import java.io.ByteArrayInputStream;
import java.util.*;

public class MainActivity extends Activity {
    private final int bg=Color.rgb(16,24,32), panel=Color.rgb(27,39,50), accent=Color.rgb(54,214,181), fg=Color.rgb(239,245,247);
    private WebView web; private EditText address; private ProgressBar progress; private boolean blocking=true; private int blocked=0;
    private final Set<String> domains=new HashSet<>(Arrays.asList("doubleclick.net","googlesyndication.com","googleadservices.com","adservice.google.com","adnxs.com","adsrvr.org","adform.net","taboola.com","outbrain.com","scorecardresearch.com","quantserve.com","criteo.com","casalemedia.com","pubmatic.com","openx.net","rubiconproject.com","advertising.com","adroll.com","hotjar.com","fullstory.com","mouseflow.com","analytics.tiktok.com"));
    @Override public void onCreate(Bundle b){super.onCreate(b); blocking=getPreferences(0).getBoolean("blocking",true); build(); setup(); web.loadUrl("https://www.google.com/");}
    private int dp(int n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}
    private GradientDrawable rounded(int c){GradientDrawable d=new GradientDrawable();d.setColor(c);d.setCornerRadius(dp(16));return d;}
    private Button button(String t){Button b=new Button(this);b.setText(t);b.setTextColor(fg);b.setTextSize(22);b.setAllCaps(false);b.setBackgroundColor(Color.TRANSPARENT);return b;}
    private void build(){
        LinearLayout root=new LinearLayout(this);root.setOrientation(1);root.setBackgroundColor(bg);
        LinearLayout bar=new LinearLayout(this);bar.setGravity(Gravity.CENTER_VERTICAL);bar.setPadding(dp(8),dp(8),dp(8),dp(8));
        TextView logo=new TextView(this);logo.setText("◈");logo.setTextColor(accent);logo.setTextSize(25);bar.addView(logo,new LinearLayout.LayoutParams(dp(34),dp(44)));
        address=new EditText(this);address.setSingleLine(true);address.setTextSize(14);address.setTextColor(fg);address.setHintTextColor(0xffa5b6be);address.setHint("Cari atau masukkan alamat");address.setPadding(dp(12),0,dp(12),0);address.setBackground(rounded(panel));address.setImeOptions(EditorInfo.IME_ACTION_GO);bar.addView(address,new LinearLayout.LayoutParams(0,dp(44),1));
        Button menu=button("⋮");bar.addView(menu,new LinearLayout.LayoutParams(dp(42),dp(44)));menu.setOnClickListener(v->menu());root.addView(bar);
        progress=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);progress.setMax(100);progress.setProgressTintList(android.content.res.ColorStateList.valueOf(accent));root.addView(progress,new LinearLayout.LayoutParams(-1,dp(3)));
        web=new WebView(this);root.addView(web,new LinearLayout.LayoutParams(-1,0,1));
        LinearLayout nav=new LinearLayout(this);nav.setGravity(Gravity.CENTER);nav.setBackgroundColor(panel);
        String[] labels={"‹","›","⌂","☆","↻"};for(String label:labels){Button b=button(label);nav.addView(b,new LinearLayout.LayoutParams(0,dp(48),1));b.setOnClickListener(v->{switch(label){case "‹":if(web.canGoBack())web.goBack();break;case "›":if(web.canGoForward())web.goForward();break;case "⌂":web.loadUrl("https://www.google.com/");break;case "☆":bookmark();break;case "↻":web.reload();break;}});}root.addView(nav);setContentView(root);
        address.setOnEditorActionListener((v,a,e)->{if(a==EditorInfo.IME_ACTION_GO||(e!=null&&e.getKeyCode()==KeyEvent.KEYCODE_ENTER)){navigate(address.getText().toString());((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(address.getWindowToken(),0);return true;}return false;});
    }
    private void setup(){WebSettings s=web.getSettings();s.setJavaScriptEnabled(true);s.setDomStorageEnabled(true);s.setAllowFileAccess(false);s.setAllowContentAccess(false);s.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);s.setSafeBrowsingEnabled(true);s.setSupportZoom(true);s.setBuiltInZoomControls(true);s.setDisplayZoomControls(false);
        web.setWebChromeClient(new WebChromeClient(){@Override public void onProgressChanged(WebView v,int p){progress.setProgress(p);progress.setVisibility(p<100?View.VISIBLE:View.GONE);}});
        web.setWebViewClient(new WebViewClient(){@Override public boolean shouldOverrideUrlLoading(WebView v,WebResourceRequest r){String scheme=r.getUrl().getScheme();if("http".equalsIgnoreCase(scheme)||"https".equalsIgnoreCase(scheme))return false;try{startActivity(new Intent(Intent.ACTION_VIEW,r.getUrl()));}catch(Exception ignored){}return true;}
        @Override public WebResourceResponse shouldInterceptRequest(WebView v,WebResourceRequest r){if(blocking&&isBlocked(r.getUrl().getHost())){synchronized(this){blocked++;}return new WebResourceResponse("text/plain","UTF-8",new ByteArrayInputStream(new byte[0]));}return super.shouldInterceptRequest(v,r);}
        @Override public void onPageStarted(WebView v,String url,android.graphics.Bitmap icon){address.setText(url);}
        @Override public void onPageFinished(WebView v,String url){address.setText(url);}});
    }
    private boolean isBlocked(String host){if(host==null)return false;String h=host.toLowerCase(Locale.ROOT);for(String d:domains)if(h.equals(d)||h.endsWith("."+d))return true;return false;}
    private void navigate(String input){String t=input.trim();if(t.isEmpty())return;if(t.matches("(?i)^[a-z][a-z0-9+.-]*://.*"))web.loadUrl(t);else if(t.contains(" ")||!t.contains("."))web.loadUrl("https://www.google.com/search?q="+Uri.encode(t));else web.loadUrl("https://"+t);}
    private void bookmark(){String url=web.getUrl();if(url==null)return;android.content.SharedPreferences p=getSharedPreferences("bookmarks",0);Set<String>s=new HashSet<>(p.getStringSet("urls",new HashSet<>()));s.add(url);p.edit().putStringSet("urls",s).apply();Toast.makeText(this,"Bookmark disimpan",0).show();}
    private void menu(){String[] opts={"Bookmark tersimpan","Pemblokiran iklan/pelacak: "+(blocking?"AKTIF":"NONAKTIF"),"Jumlah permintaan diblokir: "+blocked,"Hapus cookie dan data situs","Beranda"};new AlertDialog.Builder(this).setTitle("ShieldSurf").setItems(opts,(d,n)->{if(n==0)showBookmarks();else if(n==1){blocking=!blocking;getPreferences(0).edit().putBoolean("blocking",blocking).apply();Toast.makeText(this,"Pemblokiran "+(blocking?"aktif":"nonaktif"),0).show();}else if(n==2)new AlertDialog.Builder(this).setMessage("Permintaan domain yang diblokir sejak aplikasi dibuka: "+blocked).setPositiveButton("OK",null).show();else if(n==3)clearData();else web.loadUrl("https://www.google.com/");}).show();}
    private void showBookmarks(){Set<String>s=getSharedPreferences("bookmarks",0).getStringSet("urls",new HashSet<>());if(s.isEmpty()){Toast.makeText(this,"Belum ada bookmark",0).show();return;}String[] a=s.toArray(new String[0]);new AlertDialog.Builder(this).setTitle("Bookmark").setItems(a,(d,n)->web.loadUrl(a[n])).setNegativeButton("Tutup",null).show();}
    private void clearData(){new AlertDialog.Builder(this).setTitle("Hapus data situs?").setMessage("Cache, cookie dan data lokal situs akan dihapus. Bookmark tetap tersimpan.").setNegativeButton("Batal",null).setPositiveButton("Hapus",(d,w)->{web.clearCache(true);web.clearHistory();CookieManager.getInstance().removeAllCookies(v->{CookieManager.getInstance().flush();Toast.makeText(this,"Data situs dibersihkan",0).show();});}).show();}
    @Override public void onBackPressed(){if(web!=null&&web.canGoBack())web.goBack();else super.onBackPressed();}
}
