package com.nj.nedis.reply;

import com.nj.nedis.exception.ExceptionCode;
import com.nj.nedis.exception.ReplyException;
import com.nj.nedis.protocol.Protocol;

/**
 * Created by admin on 2018/7/7.
 */
public abstract class ComplexReply extends Protocol implements Reply {


    protected void checkComplete() {
        if (!isComplete()) {
            throw new ReplyException(ExceptionCode.REPLY_NOT_COMPLETE);
        }
    }
}
