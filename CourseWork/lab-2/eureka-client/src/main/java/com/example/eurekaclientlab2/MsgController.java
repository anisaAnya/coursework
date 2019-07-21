package com.example.eurekaclientlab2;

import com.example.CatMessage;
import com.example.OperationType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@EnableDiscoveryClient
public class MsgController {

    @Autowired
    private MsgProducer producer;

    private static Logger log = LoggerFactory.getLogger(EurekaClientLab2Application.class);

    private final RestTemplate restTemplate;

    @Autowired
    public MsgController(RestTemplateBuilder restTemplateBuilder,
                         RestTemplateResponseErrorHandler myResponseErrorHandler
    ) {

        this.restTemplate = restTemplateBuilder
                .errorHandler(myResponseErrorHandler)
                .build();
    }

    @Autowired
    private LoadBalancerClient client;

    @Autowired
    private Environment env;

    @PostMapping(value = "/refreshing", produces = "application/json; charset=UTF-8")
    public String checkRefresh() throws JsonProcessingException
    {
        return refresh() + getPropertiesClient();
    }

    @PostMapping(value = "/actuator/bus-refresh", produces = "application/json; charset=UTF-8")
    public String refresh()
    {
        return "Refreshed";
    }

    @GetMapping(value = "/properties", produces = "application/json; charset=UTF-8")
    public String getPropertiesClient() throws JsonProcessingException
    {
        Map<String, Object> props = new HashMap<>();
        CompositePropertySource bootstrapProperties = (CompositePropertySource)  ((AbstractEnvironment) env).getPropertySources().get("bootstrapProperties");
        for (String propertyName : bootstrapProperties.getPropertyNames()) {
            props.put(propertyName, bootstrapProperties.getProperty(propertyName));
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return mapper.writeValueAsString(props);
    }

    @RequestMapping(value = "/instances")
    public String getInstancesRun(){
        ServiceInstance instance = client.choose("Lab-1-MS");
        return instance.getUri().toString();
    }

    @GetMapping(value = "/kek", produces = "application/json")
    @CrossOrigin(origins = "http://localhost:3000")
    public StringResponse welcome() {
        StringResponse lol = new StringResponse("kek");
        return lol;
    }

    @PostMapping("api/Upload/")
    @CrossOrigin(origins = "http://localhost:3001")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        String url = getInstancesRun();
        log.info(file.getContentType());
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        try {
            map.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        restTemplate.exchange(String.format("%s/api/Upload/", url), HttpMethod.POST, requestEntity, String.class);

        //send request to python micro-service
        String name = file.getOriginalFilename();
        String response = this.restTemplate.exchange(String.format("http://127.0.0.1:5000/Upload/%s", name),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();
        CatMessage msg = new CatMessage("Classification happened", OperationType.GET, "200", "");
        producer.sendCatMsg(msg);
        return "<!DOCTYPE html>\n" +
                "<head>\n" +
                "    <h1>"+ response + "</h1> " +
                "<form action='http://localhost:3000'>" +
                "<input type='submit' value='Go back' />" +
                "</form>" +
                "</head>\n" +
                "</html>";
    }

    @RequestMapping(value="/info-producer",method=RequestMethod.GET,produces="application/json")
    public String info()
    {
        ObjectNode root = producer.info();

        return root.toString();
    }
}

class StringResponse {

    private String username;

    public StringResponse(String s) {
        this.username = s;
    }
    public String getUsername() {
        return this.username;
    }

    // get/set omitted...
}

class MultipartInputStreamFileResource extends InputStreamResource {

    private final String filename;

    MultipartInputStreamFileResource(InputStream inputStream, String filename) {
        super(inputStream);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() throws IOException {
        return -1; // we do not want to generally read the whole stream into memory ...
    }
}


