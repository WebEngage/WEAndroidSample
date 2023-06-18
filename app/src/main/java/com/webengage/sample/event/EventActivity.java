package com.webengage.sample.event;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.webengage.sample.R;
import com.webengage.sample.Utils.Constants;
import com.webengage.sdk.android.WebEngage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventActivity extends Activity {

    private TextInputLayout mEventNameEditText, mEventAttributeName, mEventAttributeValue, mEventAttributeType;
    private Button mTrackEventButton;
    private TextView mEventAttributeTextView;
    private FloatingActionButton mAddEventAttributesFAB;
    private Map<String, Object> eventAttributes = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        initViews();
    }

    private void initViews() {
        mEventNameEditText = findViewById(R.id.eventNameEditText);
        mTrackEventButton = findViewById(R.id.eventTrackButton);
        mEventAttributeTextView = findViewById(R.id.attributesMapTextView);
        mAddEventAttributesFAB = findViewById(R.id.addEventAttributeFAB);

        mAddEventAttributesFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> attributes = getProductMap();
                eventAttributes.putAll(attributes);
                mEventAttributeTextView.setText(attributes.toString());
            }
        });

        mTrackEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = mEventNameEditText.getEditText().getText().toString().trim();
                if (!TextUtils.isEmpty(eventName)) {
                    if (eventAttributes.isEmpty())
                        trackEvent(eventName);
                    else
                        trackEventWithAttributes(eventName, eventAttributes);
                }
            }
        });
    }

    private Map<String, Object> getProductMap(){
        Map<String, Object> product = new HashMap();
        List<Map> products = new ArrayList();
        Map<String, Object> productMap = new HashMap<>();
        Map<String, Object> deliveryAddress = new HashMap();

        product.put("ID", "104");
        product.put("Product Name", "Acer Laptop");
        product.put("Price", 3000);
        products.add(product);
        deliveryAddress.put("City", "San Francisco");
        deliveryAddress.put("ZIP", "94121");
        productMap.put("Order ID", 100);
        productMap.put("Products", products);
        productMap.put("Delivery Address", deliveryAddress);
        productMap.put("Total Amount", 500);
        productMap.put("Coupons Applied", Arrays.asList(new String[]{"GET100", "GET150"}));
        productMap.put("Discount", 20);
        productMap.put("Amount Payable", 100);

        return productMap;

    }
    private void trackEvent(String eventName) {
        WebEngage.get().analytics().track(eventName);
    }

    private void trackEventWithAttributes(String eventName, Map<String, Object> eventAttributesMap) {
        WebEngage.get().analytics().track(eventName, eventAttributesMap);
    }

    private Map<String, Object> addAttribute(Map<String, Object> map, String attributeName, String attributeValue, ATTRIBUTE_TYPE attributeType) {

        try {
            switch (attributeType) {
                case STRING:
                    map.put(attributeName, attributeValue);
                    break;
                case INTEGER:
                    map.put(attributeName, Integer.valueOf(attributeValue));
                    break;
                case FLOAT:
                    map.put(attributeName, Float.valueOf(attributeValue));
                    break;
                case LONG:
                    map.put(attributeName, Long.valueOf(attributeValue));
                    break;
                case BOOLEAN:
                    map.put(attributeName, Boolean.valueOf(attributeValue));
                    break;
                case DATE:
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    Date date = format.parse(attributeValue);
                    map.put(attributeName, date);
                    break;
            }
        } catch (Exception e) {
            Log.e(Constants.TAG, "Exception while casting event attribute " + attributeName + " to type " + attributeType.name());
        }
        return map;
    }

    //TODO List and map to be added
    enum ATTRIBUTE_TYPE {
        STRING,
        FLOAT,
        INTEGER,
        LONG,
        BOOLEAN,
        DATE,
    }
}
