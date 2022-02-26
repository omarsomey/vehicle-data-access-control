package cyber.tf.accesslogservice.controller;

import cyber.tf.accesslogservice.model.LogEntry;
import cyber.tf.accesslogservice.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    LogService logService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void log(@RequestBody LogEntry entry) {
        logService.storeEntry(entry);
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public LogEntry queryLastAccessSubject(@RequestBody LogEntry entry) {
        return logService.queryLastAccess(entry);
    }
}
