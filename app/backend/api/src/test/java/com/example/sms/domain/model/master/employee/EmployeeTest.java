package com.example.sms.domain.model.master.employee;

import com.example.sms.domain.type.FaxNumber;
import com.example.sms.domain.type.PhoneNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("社員")
class EmployeeTest {

    @Test
    @DisplayName("社員を作成できる")
    void shouldCreateEmployee() {
        Employee employee = Employee.of("EMP123", "山田 太郎", "ヤマダ タロウ", "0123456789", "03-1234-5678", "jcode");

        assertAll(
                () -> assertEquals("EMP123", employee.getEmpCode().getValue()),
                () -> assertEquals("山田 太郎", employee.getEmpName().Name()),
                () -> assertEquals("ヤマダ タロウ", employee.getEmpName().NameKana()),
                () -> assertEquals("01-2345-6789", employee.getTel().getValue()),
                () -> assertEquals("03-1234-5678", employee.getFax().getValue()),
                () -> assertEquals("jcode", employee.getOccuCode().getValue())
        );
    }

    @Nested
    @DisplayName("社員コード")
    class EmployeeCodeTest {
        @Test
        @DisplayName("社員コードはEMPから始まる5桁の数字で作成できる")
        void shouldCreateEmployeeCode() {
            assertDoesNotThrow(() -> EmployeeCode.of("EMP123"));
            assertThrows(IllegalArgumentException.class, () -> EmployeeCode.of("EMP"));
            assertThrows(IllegalArgumentException.class, () -> EmployeeCode.of("EMP1234"));
            assertThrows(IllegalArgumentException.class, () -> EmployeeCode.of("EMP123a"));
        }
    }

    @Nested
    @DisplayName("社員名")
    class EmployeeNameTest {
        @Test
        @DisplayName("社員名は姓と名をスペース区切りで作成できる")
        void shouldCreateEmployeeName() {
            assertDoesNotThrow(() -> EmployeeName.of("", ""));
            assertDoesNotThrow(() -> EmployeeName.of("山田 太郎", "ヤマダ タロウ"));
            assertDoesNotThrow(() -> EmployeeName.of("山田　太郎", "ヤマダ　タロウ"));
            assertThrows(IllegalArgumentException.class, () -> EmployeeName.of("山田", "ヤマダ タロウ"));
            assertThrows(IllegalArgumentException.class, () -> EmployeeName.of("山田 太郎", "ヤマダ"));
            assertThrows(IllegalArgumentException.class, () -> EmployeeName.of("山田", "ヤマダ"));
        }
    }

    @Nested
    @DisplayName("電話番号")
    class PhoneNumberTest {
        @Test
        @DisplayName("電話番号は0から始まる10桁または11桁の数字で作成できる")
        void shouldCreatePhoneNumber() {
            assertDoesNotThrow(() -> PhoneNumber.of("0123456789"));
            assertDoesNotThrow(() -> PhoneNumber.of("03-9999-0000"));
            assertDoesNotThrow(() -> PhoneNumber.of("01234567890"));
            assertDoesNotThrow(() -> PhoneNumber.of("090 9999 0000"));
            assertThrows(IllegalArgumentException.class, () -> PhoneNumber.of("123456789"));
            assertThrows(IllegalArgumentException.class, () -> PhoneNumber.of("01234567890a"));
        }
    }

    @Nested
    @DisplayName("FAX番号")
    class FaxNumberTest {
        @Test
        @DisplayName("FAX番号は電話番号と同じ形式で作成できる")
        void shouldCreateFaxNumber() {
            assertDoesNotThrow(() -> FaxNumber.of("0123456789"));
            assertDoesNotThrow(() -> FaxNumber.of("03-9999-0000"));
            assertDoesNotThrow(() -> FaxNumber.of("01234567890"));
            assertDoesNotThrow(() -> FaxNumber.of("090 9999 0000"));
            assertThrows(IllegalArgumentException.class, () -> FaxNumber.of("123456789"));
            assertThrows(IllegalArgumentException.class, () -> FaxNumber.of("01234567890a"));
        }
    }

}
