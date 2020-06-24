package br.com.amedigital.weather.api.config.webclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.Arrays;
import java.util.stream.Collectors;

import static io.netty.util.internal.PlatformDependent.allocateUninitializedArray;
import static io.netty.util.internal.StringUtil.CARRIAGE_RETURN;
import static io.netty.util.internal.StringUtil.NEWLINE;
import static java.lang.Math.max;
import static java.nio.charset.Charset.defaultCharset;


/**
 * Webflux does not have a good log system for webclient, that why we have to parse the Buffer and get everything from there :(
 * Ex: https://www.baeldung.com/spring-log-webclient-calls
 */
public class WebClientLogs extends LoggingHandler {

    private static final Logger LOG = LoggerFactory.getLogger(WebClientLogs.class);

    public static final String HEADER = "======================= BEGIN =======================";
    public static final String FOOTER = "======================= END   =======================";
    public static final String PRETTY_LOG_FORMAT = "\n\n%s\n%s\n%s\n";

    private final StringBuilder logText = new StringBuilder();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String format = format(ctx, msg);
        if (StringUtils.isNotEmpty(format)) {
            logText.append(format);
            LOG.info(logText.toString());
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        String format = format(ctx, msg);

        if (StringUtils.isNotEmpty(format)) {
            logText.append(format);
        }

        ctx.write(msg, promise);
    }

    protected String format(ChannelHandlerContext ctx, Object arg) {
        if (arg instanceof ByteBuf) {
            return bufferToMessage((ByteBuf) arg);
        }
        return super.format(ctx, "", arg);
    }

    private String bufferToMessage(ByteBuf msg) {
        int len = msg.readableBytes();
        String result = StringUtils.EMPTY;
        if (len != 0) {
            String allParameters = parseByteBuffer(msg, len);
            result = prettifyText(allParameters);
        }
        return result;
    }

    /** just add a header and a footer to separate each call */
    private String prettifyText(String allParameters) {
        return String.format(PRETTY_LOG_FORMAT, HEADER, splitParameters(allParameters), FOOTER);
    }

    /** Read a ByteBuffer and transforms to string */
    private String parseByteBuffer(ByteBuf msg, int len) {
        byte[] array;
        int offset = 0;
        if (msg.hasArray()) {
            array = msg.array();
            offset = msg.arrayOffset() + msg.readerIndex();
        } else {
            array = allocateUninitializedArray(max(len, 1024));
            msg.getBytes(msg.readerIndex(), array, 0, len);
        }
        return new String(array, offset, len, defaultCharset());
    }

    /** Split the parameters and filter the ones we don't want */
    private String splitParameters(String formatted) {
        return Arrays.stream(formatted.split(String.valueOf(CARRIAGE_RETURN)))
                .filter(s -> !StringUtils.contains(s, HttpHeaders.AUTHORIZATION))
                .filter(s -> !StringUtils.equals(s, NEWLINE))
                .collect(Collectors.joining());
    }

    // ==========================================
    // == NOT IN USE, JUST OVERRIDE TO BLOCK THE LOG
    // ==========================================

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) {
        ctx.fireChannelWritabilityChanged();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        ctx.fireChannelUnregistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ctx.fireChannelInactive();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.fireChannelReadComplete();
    }

    @Override
    public void flush(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) {
        ctx.bind(localAddress, promise);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        ctx.connect(remoteAddress, localAddress, promise);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) {
        ctx.disconnect(promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) {
        ctx.close(promise);
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) {
        ctx.deregister(promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) {
        ctx.read();
    }
}