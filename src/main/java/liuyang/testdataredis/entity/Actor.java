package liuyang.testdataredis.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author liuyang
 * @scine 2021/4/15
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class Actor implements Serializable {

    private static final long serialVersionUID = 1L;

    // @TableId(value = "actor_id", type = IdType.AUTO)
    private Integer actorId;

    private String firstName;

    private String lastName;

    private LocalDateTime lastUpdate;

}

