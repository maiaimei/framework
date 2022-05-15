package cn.maiaimei.framework.beans;

import java.util.LinkedHashMap;

public class ErrorMap<K, V> extends LinkedHashMap<K, V> {

    private ErrorMap() {

    }

    public static class Builder {
        private Object code;
        private Object message;
        private Object traceId;
        private Object trace;
        private Object path;

        public Builder() {
        }

        public Builder code(Object code) {
            this.code = code;
            return this;
        }

        public Builder message(Object message) {
            this.message = message;
            return this;
        }

        public Builder traceId(Object traceId) {
            this.traceId = traceId;
            return this;
        }

        public Builder trace(Object trace) {
            this.trace = trace;
            return this;
        }

        public Builder path(Object path) {
            this.path = path;
            return this;
        }

        public ErrorMap build() {
            ErrorMap map = new ErrorMap();
            map.put("code", this.code != null ? this.code : ResultStatus.INTERNAL_SERVER_ERROR.value());
            map.put("message", this.message);
            map.put("traceId", this.traceId);
            map.put("trace", this.trace);
            map.put("path", this.path);
            return map;
        }
    }

    public static ErrorMap.Builder builder() {
        return new Builder();
    }
}
