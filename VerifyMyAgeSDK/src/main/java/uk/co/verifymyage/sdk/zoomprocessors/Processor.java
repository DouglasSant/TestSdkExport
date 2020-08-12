package uk.co.verifymyage.sdk.zoomprocessors;

public abstract class Processor {
    public abstract boolean isSuccess();

    public interface SessionTokenErrorCallback {
        void onError();
    }
}

