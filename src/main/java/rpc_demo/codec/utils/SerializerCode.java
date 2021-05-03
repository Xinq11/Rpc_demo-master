package rpc_demo.codec.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SerializerCode {
    KROY(0),
    JSON(1);
    private final int code;
}
