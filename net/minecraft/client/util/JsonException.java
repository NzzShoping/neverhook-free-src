package net.minecraft.client.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class JsonException extends IOException {
    private final List<JsonException.Entry> entries = Lists.newArrayList();
    private final String message;

    public JsonException(String messageIn) {
        this.entries.add(new JsonException.Entry());
        this.message = messageIn;
    }

    public JsonException(String messageIn, Throwable cause) {
        super(cause);
        this.entries.add(new JsonException.Entry());
        this.message = messageIn;
    }

    public void prependJsonKey(String p_151380_1_) {
        this.entries.get(0).addJsonKey(p_151380_1_);
    }

    public void setFilenameAndFlush(String p_151381_1_) {
        (this.entries.get(0)).filename = p_151381_1_;
        this.entries.add(0, new JsonException.Entry());
    }

    public String getMessage() {
        return "Invalid " + this.entries.get(this.entries.size() - 1) + ": " + this.message;
    }

    public static JsonException forException(Exception p_151379_0_) {
        if (p_151379_0_ instanceof JsonException) {
            return (JsonException) p_151379_0_;
        } else {
            String s = p_151379_0_.getMessage();

            if (p_151379_0_ instanceof FileNotFoundException) {
                s = "File not found";
            }

            return new JsonException(s, p_151379_0_);
        }
    }

    public static class Entry {
        private String filename;
        private final List<String> jsonKeys;

        private Entry() {
            this.jsonKeys = Lists.newArrayList();
        }

        private void addJsonKey(String p_151373_1_) {
            this.jsonKeys.add(0, p_151373_1_);
        }

        public String getJsonKeys() {
            return StringUtils.join(this.jsonKeys, "->");
        }

        public String toString() {
            if (this.filename != null) {
                return this.jsonKeys.isEmpty() ? this.filename : this.filename + " " + this.getJsonKeys();
            } else {
                return this.jsonKeys.isEmpty() ? "(Unknown file)" : "(Unknown file) " + this.getJsonKeys();
            }
        }
    }
}
