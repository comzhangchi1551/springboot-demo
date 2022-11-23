package com.miya;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    public static ExcelModel toExcelModel(HttpModel httpModel, String code){
        ExcelModel excelModel = new ExcelModel();


        excelModel.setCode(code);

        if (httpModel == null || CollectionUtils.isEmpty(httpModel.getObj())) {
            excelModel.setGiftBool("否");
            excelModel.setGiftReason("未解析出结果");
            excelModel.setCouponBool("否");
            excelModel.setCouponReason("未解析出结果");
            excelModel.setPriceBool("否");
            excelModel.setPriceReason("未解析出结果");
            return excelModel;
        }

        List<Child> modelObj = httpModel.getObj();
        Map<String, List<Child>> parseTypeMap = modelObj.stream().collect(Collectors.groupingBy(Child::getParseTypeEnum));

        List<Child> giveaway = parseTypeMap.get("GIVEAWAY");
        List<Child> price = parseTypeMap.get("PRICE");
        List<Child> coupon = parseTypeMap.get("COUPON");


        // 赠品
        if (CollectionUtils.isEmpty(giveaway)) {
            if (CollectionUtils.isEmpty(price) && CollectionUtils.isEmpty(coupon)) {
                excelModel.setGiftBool("是");
                excelModel.setGiftReason("");
            } else {
                excelModel.setGiftBool("否");
                excelModel.setGiftReason("解析不全");
            }

        } else {

            if (giveaway.size() == 1) {
                excelModel.setGiftBool("否");
                excelModel.setGiftReason("赠品解析不全");
            }

            if (giveaway.size() == 2) {
                int asInt = giveaway.stream().mapToInt(o->o.getParseSpecification() == null ? 0 : o.getParseSpecification()).max().getAsInt();
                if (asInt >= 100) {
                    excelModel.setGiftBool("否");
                    excelModel.setGiftReason("解析错误");
                } else {
                    excelModel.setGiftBool("是");
                    excelModel.setGiftReason("");
                }
            }

            if (giveaway.size() == 3) {
                int asInt = giveaway.stream().mapToInt(o->o.getParseSpecification() == null ? 0 : o.getParseSpecification()).max().getAsInt();
                if (asInt >= 100) {
                    excelModel.setGiftBool("否");
                    excelModel.setGiftReason("主品解析为赠品");
                } else {
                    excelModel.setGiftBool("是");
                    excelModel.setGiftReason("");
                }

            }

            if (giveaway.size() > 3) {
                excelModel.setGiftBool("否");
                excelModel.setGiftReason("赠品解析过多");
            }

        }


        // 券
        if (CollectionUtils.isEmpty(price)) {
            excelModel.setPriceBool("是");
            excelModel.setPriceReason("");
        } else {
            if (price.size() > 1) {
                excelModel.setPriceBool("否");
                excelModel.setPriceReason("解析错误");
            }else {
                Child child = price.get(0);
                if (child.getMatchValue() < 10) {
                    excelModel.setPriceBool("否");
                    excelModel.setPriceReason("解析错误");
                }
                if (child.getMatchValue() % 5 != 0) {
                    excelModel.setPriceBool("否");
                    excelModel.setPriceReason("解析错误");
                } else {
                    excelModel.setPriceBool("是");
                    excelModel.setPriceReason("");
                }
            }

        }


        // 满减
        if (CollectionUtils.isEmpty(coupon)) {
            excelModel.setCouponBool("是");
            excelModel.setCouponReason("");
        } else {
            if (coupon.size() > 1) {
                excelModel.setCouponBool("否");
                excelModel.setCouponReason("价格解析为券");
            }

            if (coupon.size() == 1) {
                Child child = coupon.get(0);
                if (child.getMatchValue() == 10) {
                    excelModel.setCouponBool("否");
                    excelModel.setCouponReason("解析错误");
                } else if (child.getMatchValue() > 200) {
                    excelModel.setCouponBool("否");
                    excelModel.setCouponReason("价格解析为券");
                } else if (child.getMatchValue() % 5 != 0) {
                    excelModel.setCouponBool("否");
                    excelModel.setCouponReason("解析错误");
                }else {
                    excelModel.setCouponBool("是");
                    excelModel.setCouponReason("");
                }

            }

        }

        return excelModel;
    }
}
