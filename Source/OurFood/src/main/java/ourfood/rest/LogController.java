package ourfood.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ourfood.domain.User;
import ourfood.service.LogService;

/**
 * This class is responsible to log all the activities in the app.
 *
 * 
 * @author raghu.mulukoju
 * 
 */

@RequestMapping(value = "/rest/logs")
@RestController("restLogController")
@PreAuthorize("isAuthenticated()")
public class LogController {

    @Autowired
    LogService logService;

    @RequestMapping(value = "/action", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public String actionAccessLog(@RequestParam Long appliteId, @RequestParam Long actionId,
            @RequestParam Long timeStamp, Authentication auth) {

        User user = (User) auth.getPrincipal();
        Long userId = user.getId();

        this.logService.logAction(appliteId, actionId, userId, timeStamp);

        return "";
    }
}