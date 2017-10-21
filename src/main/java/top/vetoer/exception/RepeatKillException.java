package top.vetoer.exception;

/**
 * 重复秒杀异常,是一个运行期异常,不需要我们手动try catch
 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }


    public RepeatKillException(String message) {
        super(message);
    }
}
