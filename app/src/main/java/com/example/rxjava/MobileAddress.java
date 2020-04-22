package com.example.rxjava;

/**
 * @ProjectName: RxJava2Test
 * @Package: com.example.rxjava
 * @ClassName: MobileAddress
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2020/4/18 16:53
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/18 16:53
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MobileAddress {
    private Long error_code;
    private String reason;

    public Long getError_code() {
        return error_code;
    }

    public void setError_code(Long error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
