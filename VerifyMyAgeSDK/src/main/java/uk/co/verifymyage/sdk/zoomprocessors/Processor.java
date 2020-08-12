package uk.co.verifymyage.sdk.ZoomProcessors;

public abstract class Processor {
    public abstract boolean isSuccess();

    public interface SessionTokenErrorCallback {
        void onError();
    }
}

