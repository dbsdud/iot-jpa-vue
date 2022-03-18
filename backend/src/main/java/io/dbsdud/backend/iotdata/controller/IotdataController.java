package io.dbsdud.backend.iotdata.controller;

import io.dbsdud.backend.common.model.PageRequest;
import io.dbsdud.backend.devices.domain.Device;
import io.dbsdud.backend.iotdata.domain.Iotdata;
import io.dbsdud.backend.iotdata.dto.IotdataDTO;
import io.dbsdud.backend.iotdata.service.IotdataService;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
@Configuration
public class IotdataController {

    private final IotdataService iotdataService;

    private static final String BROKER_URL = "tcp://MQTT_SERVER_IP:1883";
    private static final String MQTT_CLIENT_ID = MqttAsyncClient.generateClientId();
    private static final String TOPIC_FILTER = "devices/#";

    @Bean
    public MessageChannel mqttInputChannel() {

        return new DirectChannel();
    }

    @Bean
    public MessageProducer inboundChannel() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(BROKER_URL, MQTT_CLIENT_ID, TOPIC_FILTER);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler inboundMessageHandler() {
        return message -> {
            String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
            String payload = (String) message.getPayload();
            /*System.out.println(message);
            System.out.println("Topic: " + topic);*/
            System.out.println("Payload: " + payload);
            //System.out.println(message.getPayload());
            // System.out.println(payload.getClass());

            //iotdataService.getMqttData((String) message.getPayload());
            iotdataService.getMqttData(payload);
        };
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public IotdataDTO.DataResponse registerData(@RequestBody @Valid final IotdataDTO.DataRegisterRequest dto) {
        return new IotdataDTO.DataResponse(iotdataService.registerData(dto));
    }

    @GetMapping("/all")
    public Page<IotdataDTO.DataResponse> getAllData(final PageRequest pageable) {
        return iotdataService.findAll(pageable.of()).map(IotdataDTO.DataResponse::new);
    }

    @GetMapping
    public List<Iotdata> getIotdataByDevice(@RequestParam(name = "device") final Device device) {
        /*return iotdataService.findByDevice(device);*/
        return iotdataService.findFirst20ByDeviceOrderByIotdataIdDesc(device);
    }

    @GetMapping("/realtime")
    public Iotdata getRealtimeIotdataByDevice(@RequestParam(name = "device") final Device device) {
        return iotdataService.findFirst1ByDeviceOrderByIotdataIdDesc(device);
    }
}
