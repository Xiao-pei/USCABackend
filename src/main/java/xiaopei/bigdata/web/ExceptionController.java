package xiaopei.bigdata.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xiaopei.bigdata.MyException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MyException.class)
    public ResponseEntity<ObjectNode> handleNotFoundException(MyException ex) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("error", 1);
        node.put("code", ex.getErrCode());
        node.put("errmsg", ex.getMessage());
        return new ResponseEntity<>(node, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ObjectNode> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("error", 1);
        node.put("errmsg", ex.getMessage());
        return new ResponseEntity<>(node, HttpStatus.NOT_FOUND);
    }

}
