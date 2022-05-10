package com.sellertool.server.domain.excel_form.waybill;


import com.sellertool.server.domain.excel_form.waybill.dto.WaybillExcelFormDto;
import com.sellertool.server.utils.CustomFieldUtils;

import java.util.Arrays;
import java.util.List;

public class WaybillExcelFormManager {
    public static List<String> HEADER_NAMES = Arrays.asList(
            "수취인명",
            "!운송코드",
            "운송장번호",
            "배송방식",
            "택배사"
    );

    public static List<Integer> REQUIRED_CELL_NUMBERS = Arrays.asList(
            0, 1, 2
    );

    public static Integer HEADER_ROW_INDEX = 0;
    public static Integer DATA_START_ROW_INDEX = 1;
    public static Integer ALLOWED_CELL_SIZE = HEADER_NAMES.size();

    public static List<String> getAllFieldNames() {
        return CustomFieldUtils.getAllFieldNames(new WaybillExcelFormDto());
    }
}
