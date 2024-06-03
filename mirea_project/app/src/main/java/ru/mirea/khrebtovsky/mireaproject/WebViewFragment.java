package ru.mirea.khrebtovsky.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class WebViewFragment extends Fragment {

    private WebView webView;
    private EditText urlEditText;
    private Button goButton;

    public WebViewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        webView = view.findViewById(R.id.webview);
        urlEditText = view.findViewById(R.id.url_edit_text);
        goButton = view.findViewById(R.id.go_button);


        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("https://vk.com");

        webView.setWebViewClient(new WebViewClient());

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlEditText.getText().toString();
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://yandex.ru/search/?text=" + url;
                }
                webView.loadUrl(url);
            }
        });

        return view;
    }
}
