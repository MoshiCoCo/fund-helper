package top.misec.service.impl;

import org.springframework.stereotype.Service;
import top.misec.common.Constants;
import top.misec.common.Result;
import top.misec.service.GetPasswordService;
import top.misec.utils.RandomString;

@Service
public class GetPasswordServiceImpl implements GetPasswordService {
    @Override
    public Result getDefaultPassword(Integer len) {

        if (len != null) {
            if (len < Constants.minPassWordLen) {
                return Result.fail("长度不得低于8");
            }
            if (len > 512) {
                return Result.fail("你特么用的着这么长的密码吗");
            }
            return Result.success(RandomString.randomStr(len), 0, "获取成功");
        }

        return Result.success(RandomString.randomStr(Constants.defaultPassWordLen), 0, "获取成功");

    }
}
