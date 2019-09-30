package com.hfad.gameoffaces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class CustomJSONParser {
    public JSONObject object;

    public CustomJSONParser(String string) {
        try {
            this.object = new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public CustomJSONParser initialize(String s) {
        return new CustomJSONParser(s);
    }

    public List<String> listAllKeysAtCurrentLevel(int depth) {
        JSONObject copy = this.object;
        while (depth > 1) {
            Iterator iterator = copy.keys();
            try {
                copy = copy.getJSONObject(iterator.next().toString());
                depth--;
            } catch (JSONException e) {
                e.printStackTrace();
                break;
            }
        }

        ArrayList<String> keys = new ArrayList<String>();
        if (depth == 1) {
            Iterator iterator = copy.keys();
            while (iterator.hasNext()) {
                keys.add((String) iterator.next());
            }
            return keys;
        } else {
            return null;
        }
    }

    public String getJSONObject(String tag) {
        JSONObject copy = this.object;
        int depth = 1;
        if (checkIfTagExists(tag)) {
            // continue moving forward while we find the key.
            while (!listAllKeysAtCurrentLevel(depth).contains(tag)) {
                Iterator iterator = copy.keys();
                try {
                    copy = copy.getJSONObject(iterator.next().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                String string = copy.getJSONObject(tag).toString();
                return string;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<Object> getJSONArray(String tag) {
        JSONObject copy = this.object;
        int depth = 1;
        if (checkIfTagExists(tag)) {
            // continue moving forward while we find the key.
            while (!listAllKeysAtCurrentLevel(depth).contains(tag)) {
                Iterator iterator = copy.keys();
                try {
                    copy = copy.getJSONObject(iterator.next().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                JSONObject value = copy.getJSONObject(tag);
                List<Object> objects = new ArrayList<>();
                for (int i = 0; i < value.getJSONArray(tag).length(); i++) {
                    objects.add(value.getJSONArray(tag).get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean checkIfTagExists(String tag) {
        int all_depths = maxDepth();
        for (int i = 1; i < maxDepth() + 1; i++) {
            List<String> keys = listAllKeysAtCurrentLevel(i);
            if (keys.contains(tag)) {
                return true;
            }
        }
        return false;
    }

    int maxDepth() {
        int depth = 0;
        JSONObject copy = this.object;
        while (copy.keys() != null) {
            Iterator iterator = copy.keys();
            try {
                String key = (String) iterator.next();
                copy = copy.getJSONObject(key);
                depth++;
            } catch (JSONException e) {
                e.printStackTrace();
                break;
            }
        }
        return depth + 1;
    }
}
