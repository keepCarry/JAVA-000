package io.kimmking.rpcfx.exception;

public class RpcfxServerException  extends RpcfxException {

    public RpcfxServerException(String msg) {
        super(msg);
    }

    public RpcfxServerException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
