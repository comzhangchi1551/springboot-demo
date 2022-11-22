package com.miya;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Data
public class HttpModel {
    private String code;
    private String message;
    private Boolean success;
    private List<String> errors;
    private List<Child> obj;

    @Data
    private static class Child{
        private String parseTypeEnum;
        private String parseContent;
        private Integer parseSpecification;
        private String parseName;
        private String parseUnit;
        private Integer parseCount;
        private Integer matchValue;

    }


    /**
     * 券 ===> 普通券nullnull*1
     * 赠品 ===> 视黄醇眼霜15ml*1
     * 赠品 ===> 致美视黄醇眼霜15ml*1
     * 赠品 ===> 致美青春保湿乳50ml*1
     * 赠品 ===> 致美青春细肤露50ml*1
     * @return
     */
    @Override
    public String toString() {
        String result = "";
        if (!CollectionUtils.isEmpty(obj)) {
            obj.sort((a, b) -> a.getParseTypeEnum().compareTo(b.getParseTypeEnum()));

            for (Child child : obj) {
                String parseTypeEnum = child.getParseTypeEnum();
                switch (parseTypeEnum) {
                    case "GIVEAWAY":
                        parseTypeEnum = "赠品";
                        String parseSpecificationStr = toEmptyStr(child.parseSpecification);
                        String parseUnit = toEmptyStr(child.parseUnit);
                        result = result + parseTypeEnum + " ===> " + child.parseName + parseSpecificationStr + parseUnit + "*" + (child.parseCount == null ? 1 : child.parseCount);
                        break;
                    case "PRICE":
                        parseTypeEnum = "减免";
                        result = result + parseTypeEnum + " ===> " + child.parseName + child.matchValue;
                        break;
                    case "COUPON":
                        parseTypeEnum = "券";
                        result = result + parseTypeEnum + " ===> " + child.parseName + child.matchValue;
                        break;
                }



                result = result + "\r\n";
            }
        }

        return result;
    }

    private static String toEmptyStr(Object obj) {
        String str = obj == null ? "" : String.valueOf(obj);
        return str;
    }
}
