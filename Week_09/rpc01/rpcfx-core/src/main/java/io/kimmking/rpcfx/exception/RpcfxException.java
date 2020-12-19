package io.kimmking.rpcfx.exception;

public abstract class RpcfxException extends RuntimeException {
    public RpcfxException(String msg) {
        super(msg);
    }

    public RpcfxException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
