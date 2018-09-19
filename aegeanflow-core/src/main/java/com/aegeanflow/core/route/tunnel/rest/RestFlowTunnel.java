package com.aegeanflow.core.route.tunnel.rest;

import com.aegeanflow.core.spi.Streamable;
import com.aegeanflow.core.codec.CodecProvider;
import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.exchange.StreamExchange;
import com.aegeanflow.core.route.InputPoint;
import com.aegeanflow.core.route.StandartTunnelType;
import com.aegeanflow.core.route.tunnel.StreamTunnel;
import com.aegeanflow.core.route.tunnel.FlowTunnel;
import com.aegeanflow.core.route.tunnel.TunnelType;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RestFlowTunnel implements FlowTunnel {

    private final CodecProvider codecProvider;

    private final FlowTunnelRestClient restClient;

    @Inject
    public RestFlowTunnel(CodecProvider codecProvider, FlowTunnelRestClient restClient) {
        this.codecProvider = codecProvider;
        this.restClient = restClient;
    }

    @Override
    public <T> void accept(InputPoint<T> inputPoint, T value) {
        try {
            InputStream jsonInputStream = codecProvider.getCodec(inputPoint.getInput().type()).encoder().encode(value);
            restClient.acceptInput(inputPoint, jsonInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <E extends Exchange<T>, T> void acceptExchange(InputPoint<T> inputPoint, E value) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        try (InputStream is = codecProvider.getCodec(inputPoint.getInput().type()).encoder().encode(value.get())){
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte[] byteArray = buffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T extends Streamable<I>, I> StreamTunnel<I> acceptStreamable(InputPoint inputPoint, T value) {
        return null;
    }

    @Override
    public <E extends StreamExchange<T, I>, T extends Streamable<I>, I> StreamTunnel<I> acceptStreamExchange(InputPoint<T> inputPoint, E value) {
        return null;
    }

    @Override
    public TunnelType getTunnelType() {
        return StandartTunnelType.RESTFUL;
    }
}
