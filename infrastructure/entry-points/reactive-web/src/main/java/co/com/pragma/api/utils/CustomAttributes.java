package co.com.pragma.api.utils;

import co.com.pragma.model.capacity.exceptions.CustomException;
import co.com.pragma.model.capacity.exceptions.HttpException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String,Object> errorAttributes = new HashMap<>();
        Throwable throwable = super.getError(request);
        if (throwable instanceof HttpException httpException){
            errorAttributes.put("status", httpException.getStatus());
            errorAttributes.put("message", httpException.getMessage());
            return errorAttributes;
        }
        if (!(throwable instanceof CustomException customException)) {
            return errorAttributes;
        }
        errorAttributes.put("status", customException.getStatus());
        errorAttributes.put("message", customException.getMessage());
        return errorAttributes;
    }
}
