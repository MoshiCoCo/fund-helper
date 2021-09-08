package top.misec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.misec.common.Result;
import top.misec.service.GetPasswordService;


@RestController
@RequestMapping("/utils")
public class PwdController {

    @Autowired
    private GetPasswordService getPasswordService;

    @GetMapping(value = "/getPassword")
    public Result getPwd(@RequestParam(value = "length", required = false) Integer length) {
        return getPasswordService.getDefaultPassword(length);
    }
}
