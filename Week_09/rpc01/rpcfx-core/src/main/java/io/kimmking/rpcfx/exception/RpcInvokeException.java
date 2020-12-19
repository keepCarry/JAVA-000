package io.kimmking.rpcfx.exception;

public class RpcInvokeException extends RpcfxException {
    public RpcInvokeException(String msg) {
        super(msg);
    }

    public RpcInvokeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
