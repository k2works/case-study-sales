package com.example.sms.infrastructure;

import com.opencsv.CSVReader;
import com.opencsv.bean.AbstractBeanField;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class Pattern2ReadCSVUtil<T> {

    private static final Logger logger = Logger.getLogger(Pattern2ReadCSVUtil.class.getName());

    /**
     * CSVを1行目をヘッダーとして読み込み、2行目以降のデータを処理します。
     *
     * @param bean 型Tのクラス
     * @param multipartFile MultipartFile型CSVファイル
     * @param charset 文字コード (例: UTF-8)
     * @return パースされたデータリスト
     */
    public List<T> readCSV(Class<T> bean, MultipartFile multipartFile, String charset) {
        try (Reader reader = new InputStreamReader(multipartFile.getInputStream(), Charset.forName(charset));
             CSVReader csvReader = new CSVReader(reader)) {

            // CsvToBeanBuilderを使ってパース処理を設定
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(csvReader)
                    .withType(bean)              // マッピングするクラス
                    .withSkipLines(1)            // 1行目（ヘッダー）をスキップ
                    .withThrowExceptions(false)  // エラー時はExceptionを抑制して収集
                    .build();

            // CSVデータをパースして取得
            List<T> parsedData = csvToBean.parse();

            // エラーが発生した行の詳細をログ出力
            if (!csvToBean.getCapturedExceptions().isEmpty()) {
                logger.warning("CSVパースエラーが発生しました:");
                csvToBean.getCapturedExceptions().forEach(e -> logger.warning("エラー行: " + Arrays.toString(e.getLine()) + " | " + e.getMessage()));
            }

            // 空データやエラー行は除去してフィルタリング
            return parsedData.stream()
                    .filter(Objects::nonNull) // nullチェック
                    .toList();
        } catch (Exception ex) {
            throw new IllegalArgumentException("CSV の読み込み中にエラーが発生しました", ex);
        }
    }

    public static class LocalDateTimeConverter extends AbstractBeanField<LocalDateTime, String> {

        // Adjusting the formatter to handle 'T' in the date-time string
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        @Override
        protected LocalDateTime convert(String value) {
            if (value == null || value.isEmpty()) {
                return null; // Handle null or empty strings gracefully
            }

            try {
                // Parse the string into LocalDateTime using the correct formatter
                return LocalDateTime.parse(value, FORMATTER);
            } catch (DateTimeParseException e) {
                // Logging or other action can be taken for invalid formats
                Logger.getGlobal().severe("Failed to parse date-time: " + value + ". Error: " + e.getMessage());
                return null; // Return null to indicate parsing failure
            }
        }
    }

    public static class SafeIntegerConverter extends AbstractBeanField<Integer, String> {
        @Override
        protected Integer convert(String value) {
            try {
                return Integer.valueOf(value.trim());
            } catch (NumberFormatException | NullPointerException e) {
                return 0; // デフォルト値として 0 を返す
            }
        }
    }
}