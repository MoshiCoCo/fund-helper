package top.misec.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author JunzhouLiu
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result {
    private static final Integer SUCCESS_CODE = 0;
    private static final Integer FAIL_CODE = -1;
    private static final String EMPTY_STRING = "";
    private Integer code;
    private Object data;
    private String msg;

    public static Result success() {
        return success(EMPTY_STRING, SUCCESS_CODE, EMPTY_STRING);
    }

    public static Result success(Object data) {
        return success(data, SUCCESS_CODE, EMPTY_STRING);
    }

    public static Result success(Object data, Integer code) {
        return success(data, code, EMPTY_STRING);
    }

    public static Result success(Object data, String msg) {
        return success(data, SUCCESS_CODE, msg);
    }

    public static Result success(Integer code, String msg) {
        return success(EMPTY_STRING, code, msg);
    }

    public static Result success(Object data, Integer code, String msg) {
        return new Result(code, data, msg);
    }


    public static Result fail() {
        return fail(EMPTY_STRING, FAIL_CODE, EMPTY_STRING);
    }

    public static Result fail(String msg) {
        return fail(EMPTY_STRING, FAIL_CODE, msg);
    }

    public static Result fail(Integer code) {
        return fail(EMPTY_STRING, code, EMPTY_STRING);
    }

    public static Result fail(Integer code, String msg) {
        return fail(EMPTY_STRING, code, msg);
    }

    public static Result fail(Object data, Integer code, String msg) {
        return new Result(code, data, msg);
    }
}