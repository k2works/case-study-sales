package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.customer.Customer;
import com.example.sms.domain.model.master.partner.customer.CustomerBillingCategory;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.model.master.partner.customer.CustomerType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.master.partner.CustomerCriteria;
import com.example.sms.service.master.partner.CustomerService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

/**
 * 顧客 API
 */
@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer", description = "顧客管理")
@PreAuthorize("hasRole('ADMIN')")
public class CustomerApiController {

    private final CustomerService customerService;
    private final Message message;

    public CustomerApiController(CustomerService customerService, Message message) {
        this.customerService = customerService;
        this.message = message;
    }

    @Operation(summary = "顧客一覧を取得する")
    @GetMapping
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<Customer> result = customerService.selectAllWithPageInfo();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "顧客を取得する")
    @GetMapping("/{customerCode}/{customerBranchCode}")
    public ResponseEntity<?> select(@PathVariable("customerCode") String customerCode , @PathVariable("customerBranchCode") String customerBranchCode) {
        try {
            Customer result = customerService.find(CustomerCode.of(customerCode, Integer.valueOf(customerBranchCode)));
            if (result == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.customer.not.exist")));
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "顧客を登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.顧客登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> register(@RequestBody CustomerResource customerResource) {
        try {
            Customer customer = convertToEntity(customerResource);
            if (customerService.find(customer.getCustomerCode()) != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.customer.already.exist")));
            }
            customerService.register(customer);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.customer.registered")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "顧客を更新する")
    @PutMapping("/{customerCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.顧客更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(
            @PathVariable("customerCode") String customerCode,
            @RequestBody CustomerResource customerResource) {
        try {
            Customer customer = convertToEntity(customerResource);
            if (customerService.find(customer.getCustomerCode()) == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.customer.not.exist")));
            }
            customerService.save(customer);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.customer.updated")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "顧客を削除する")
    @DeleteMapping("/{customerCode}/{customerBranchCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.顧客削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable("customerCode") String customerCode, @PathVariable("customerBranchCode") String customerBranchCode) {
        try {
            Customer customer = customerService.find(CustomerCode.of(customerCode, Integer.valueOf(customerBranchCode)));
            if (customer == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.customer.not.exist")));
            }
            customerService.delete(customer);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.customer.deleted")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "顧客を検索する")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody CustomerCriteriaResource criteriaResource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            CustomerCriteria criteria = convertToCriteria(criteriaResource);
            PageInfo<Customer> result = customerService.searchWithPageInfo(criteria);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    private Customer convertToEntity(CustomerResource resource) {
        Customer customer = Customer.of(
                resource.getCustomerCode(),
                resource.getCustomerBranchNumber(),
                resource.getCustomerType().getValue(),
                resource.getBillingCode(),
                resource.getBillingBranchNumber(),
                resource.getCollectionCode(),
                resource.getCollectionBranchNumber(),
                resource.getCustomerName(),
                resource.getCustomerNameKana(),
                resource.getCompanyRepresentativeCode(),
                resource.getCustomerRepresentativeName(),
                resource.getCustomerDepartmentName(),
                resource.getCustomerPostalCode(),
                resource.getCustomerPrefecture(),
                resource.getCustomerAddress1(),
                resource.getCustomerAddress2(),
                resource.getCustomerPhoneNumber(),
                resource.getCustomerFaxNumber(),
                resource.getCustomerEmailAddress(),
                resource.getCustomerBillingType().getValue(),
                resource.getCustomerClosingDay1().getValue(),
                resource.getCustomerPaymentMonth1().getValue(),
                resource.getCustomerPaymentDay1().getValue(),
                resource.getCustomerPaymentMethod1().getValue(),
                resource.getCustomerClosingDay2().getValue(),
                resource.getCustomerPaymentMonth2().getValue(),
                resource.getCustomerPaymentDay2().getValue(),
                resource.getCustomerPaymentMethod2().getValue()
        );

        return Customer.of(customer, resource.getShippings());
    }

    private CustomerCriteria convertToCriteria(CustomerCriteriaResource resource) {
        return CustomerCriteria.builder()
                .customerCode(resource.getCustomerCode())
                .customerName(resource.getCustomerName())
                .customerNameKana(resource.getCustomerNameKana())
                .customerType(mapStringToCode(resource.getCustomerType(), CustomerType::getCodeByName))
                .companyRepresentativeCode(resource.getCompanyRepresentativeCode())
                .customerRepresentativeName(resource.getCustomerRepresentativeName())
                .customerDepartmentName(resource.getCustomerDepartmentName())
                .postalCode(resource.getPostalCode())
                .prefecture(resource.getPrefecture())
                .address1(resource.getAddress1())
                .address2(resource.getAddress2())
                .customerPhoneNumber(resource.getCustomerPhoneNumber())
                .customerFaxNumber(resource.getCustomerFaxNumber())
                .customerEmailAddress(resource.getCustomerEmailAddress())
                .customerBillingCategory(mapStringToCode(resource.getCustomerBillingCategory(), CustomerBillingCategory::getCodeByName))
                .build();
    }

    private <T> T mapStringToCode(String value, Function<String, T> mapper) {
        return value != null ? mapper.apply(value) : null;
    }
}