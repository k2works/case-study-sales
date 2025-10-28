package com.example.sms.infrastructure.datasource.procurement.payment;

import com.example.sms.domain.model.procurement.payment.PurchasePayment;
import com.example.sms.domain.model.procurement.payment.PurchasePaymentList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.autogen.mapper.支払データMapper;
import com.example.sms.infrastructure.datasource.autogen.model.支払データ;
import com.example.sms.service.procurement.payment.PurchasePaymentCriteria;
import com.example.sms.service.procurement.payment.PurchasePaymentRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PurchasePaymentDataSource implements PurchasePaymentRepository {
    final 支払データMapper paymentMapper;
    final PurchasePaymentCustomMapper paymentCustomMapper;
    final PurchasePaymentEntityMapper paymentEntityMapper;

    public PurchasePaymentDataSource(
            支払データMapper paymentMapper,
            PurchasePaymentCustomMapper paymentCustomMapper,
            PurchasePaymentEntityMapper paymentEntityMapper) {
        this.paymentMapper = paymentMapper;
        this.paymentCustomMapper = paymentCustomMapper;
        this.paymentEntityMapper = paymentEntityMapper;
    }

    @Override
    public PurchasePayment save(PurchasePayment payment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        Optional<PurchasePaymentCustomEntity> existingEntity = Optional.ofNullable(
                paymentCustomMapper.selectByPrimaryKey(payment.getPaymentNumber().getValue()));

        if (existingEntity.isEmpty()) {
            insertPayment(payment, username);
        } else {
            updatePayment(payment, username);
        }
        return payment;
    }

    private void insertPayment(PurchasePayment payment, String username) {
        LocalDateTime now = LocalDateTime.now();

        支払データ paymentEntity = paymentEntityMapper.toEntity(payment);
        paymentEntity.set作成日時(now);
        paymentEntity.set作成者名(username);
        paymentEntity.set更新日時(now);
        paymentEntity.set更新者名(username);

        paymentCustomMapper.insert(paymentEntity);
    }

    private void updatePayment(PurchasePayment payment, String username) {
        LocalDateTime now = LocalDateTime.now();

        PurchasePaymentCustomEntity existingEntity = paymentCustomMapper.selectByPrimaryKey(
                payment.getPaymentNumber().getValue());
        if (existingEntity == null) {
            throw new IllegalStateException("更新対象の支払データが見つかりません: " + payment.getPaymentNumber().getValue());
        }

        支払データ paymentEntity = paymentEntityMapper.toEntity(payment);
        paymentEntity.set作成日時(existingEntity.get作成日時());
        paymentEntity.set作成者名(existingEntity.get作成者名());
        paymentEntity.set更新日時(now);
        paymentEntity.set更新者名(username);

        paymentCustomMapper.updateByPrimaryKey(paymentEntity);
    }

    @Override
    public Optional<PurchasePayment> findByPaymentNumber(String paymentNumber) {
        PurchasePaymentCustomEntity entity = paymentCustomMapper.selectByPrimaryKey(paymentNumber);
        if (entity != null) {
            return Optional.of(paymentEntityMapper.toModel(entity));
        }
        return Optional.empty();
    }

    @Override
    public void delete(String paymentNumber) {
        paymentMapper.deleteByPrimaryKey(paymentNumber);
    }

    @Override
    public void deleteAll() {
        paymentCustomMapper.deleteAll();
    }

    @Override
    public PurchasePaymentList selectAll() {
        List<PurchasePaymentCustomEntity> entities = paymentCustomMapper.selectAll();
        List<PurchasePayment> payments = paymentEntityMapper.toModelList(entities);
        return PurchasePaymentList.of(payments);
    }

    @Override
    public PageInfo<PurchasePayment> selectAllWithPageInfo() {
        List<PurchasePaymentCustomEntity> paymentCustomEntities = paymentCustomMapper.selectAll();
        PageInfo<PurchasePaymentCustomEntity> pageInfo = new PageInfo<>(paymentCustomEntities);

        return PageInfoHelper.of(pageInfo, paymentEntityMapper::toModel);
    }

    @Override
    public PageInfo<PurchasePayment> searchWithPageInfo(PurchasePaymentCriteria criteria) {
        List<PurchasePaymentCustomEntity> paymentCustomEntities = paymentCustomMapper.selectByCriteria(criteria.toMap());
        PageInfo<PurchasePaymentCustomEntity> pageInfo = new PageInfo<>(paymentCustomEntities);

        return PageInfoHelper.of(pageInfo, paymentEntityMapper::toModel);
    }
}
