package com.talexframe.frame.utils;

import cn.hutool.core.util.StrUtil;
import com.talexframe.frame.core.pojo.annotations.TPParamValidator;

/**
 * {@link com.talexframe.frame.utils Package }
 *
 * @author TalexDreamSoul 22/04/22 下午 09:20 Project: TalexFrame
 */
public class ValidatorUtil {

    public static final int SUCCESS = 100;
    public static final int MISS_DATA = 0;
    public static final int MISS_PATTERN = -100;
    public static final int MISS_ASSERT = -10;
    public static final int MISS_MIN_LENGTH = -21;
    public static final int MISS_MAX_LENGTH = -22;

    public static final int MISS_MIN = -31;
    public static final int MISS_MAX = -32;

    public static int validateData(TPParamValidator validator, Object data) {

        if( validator == null ) return SUCCESS;

        if( data == null ) return validator.notNull() ? MISS_DATA : SUCCESS;

        if( !StrUtil.isBlankIfStr(validator.pattern()) ) {

            if( !data.toString().matches(validator.pattern()) ) return MISS_PATTERN;

        }

        if( validator.assertFalse() || validator.assertTrue() ) {

            if( data instanceof Boolean ) {

                boolean b = (boolean) data;

                return ((b && validator.assertTrue()) || (!b && validator.assertFalse())) ? SUCCESS : MISS_ASSERT;

            } else {

                return MISS_ASSERT;

            }

        }

        String str = (String) data;

        if( validator.maxLength() > 0 && str.length() < validator.minLength() ) return MISS_MIN_LENGTH;
        if( validator.maxLength() > 0 && str.length() > validator.maxLength() ) return MISS_MAX_LENGTH;

        if( validator.min() != Integer.MIN_VALUE ) {

            try {

                int tmp = Integer.parseInt(str);
                return tmp < validator.min() ? MISS_MIN : SUCCESS;

            } catch (NumberFormatException e) {

                return MISS_MIN;

            }

        }

        if( validator.max() != Integer.MAX_VALUE ) {

            try {

                int tmp = Integer.parseInt(str);
                return tmp > validator.max() ? MISS_MAX : SUCCESS;

            } catch (NumberFormatException e) {

                return MISS_MAX;

            }

        }

        return SUCCESS;

    }

}
