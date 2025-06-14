package com.example.sms.infrastructure.datasource.sales.invoice;

import com.example.sms.domain.model.sales.invoice.Invoice;
import com.example.sms.domain.model.sales.invoice.InvoiceList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.autogen.mapper.請求データMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.請求データ明細Mapper;
import com.example.sms.infrastructure.datasource.autogen.model.請求データ;
import com.example.sms.infrastructure.datasource.autogen.model.請求データ明細;
import com.example.sms.infrastructure.datasource.autogen.model.請求データ明細Key;
import com.example.sms.infrastructure.datasource.sales.invoice.invoice_line.InvoiceLineCustomMapper;
import com.example.sms.service.sales.invoice.InvoiceCriteria;
import com.example.sms.service.sales.invoice.InvoiceRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 請求データリポジトリ実装
 */
@Repository
public class InvoiceDataSource implements InvoiceRepository {
    private final 請求データMapper invoiceMapper;
    private final 請求データ明細Mapper invoiceLineMapper;
    private final InvoiceCustomMapper invoiceCustomMapper;
    private final InvoiceLineCustomMapper invoiceLineCustomMapper;
    private final InvoiceEntityMapper invoiceEntityMapper;

    public InvoiceDataSource(
            請求データMapper invoiceMapper, 請求データ明細Mapper invoiceLineMapper,
            InvoiceCustomMapper invoiceCustomMapper,
            InvoiceLineCustomMapper invoiceLineCustomMapper,
            InvoiceEntityMapper invoiceEntityMapper) {
        this.invoiceMapper = invoiceMapper;
        this.invoiceLineMapper = invoiceLineMapper;
        this.invoiceCustomMapper = invoiceCustomMapper;
        this.invoiceLineCustomMapper = invoiceLineCustomMapper;
        this.invoiceEntityMapper = invoiceEntityMapper;
    }

    @Override
    public void deleteAll() {
        invoiceLineCustomMapper.deleteAll();
        invoiceCustomMapper.deleteAll();
    }

    @Override
    public void save(Invoice invoice) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";
        Optional<InvoiceCustomEntity> invoiceEntity = Optional.ofNullable(invoiceCustomMapper.selectByPrimaryKey(Objects.requireNonNull(invoice.getInvoiceNumber()).getValue()));

        if (invoiceEntity.isEmpty()) {
            createInvoice(invoice, username);
        } else {
            updateInvoice(invoice, invoiceEntity, username);
        }
    }

    private void updateInvoice(Invoice invoice, Optional<InvoiceCustomEntity> invoiceEntity, String username) {
        請求データ invoiceData = invoiceEntityMapper.mapToEntity(invoice);
        if (invoiceEntity.isPresent()) {
            invoiceData.set作成日時(invoiceEntity.get().get作成日時());
            invoiceData.set作成者名(invoiceEntity.get().get作成者名());
            invoiceData.set更新日時(LocalDateTime.now());
            invoiceData.set更新者名(username);
        }

        invoiceMapper.updateByPrimaryKey(invoiceData);

        if (invoice.getInvoiceLines() != null) {
            invoiceLineCustomMapper.deleteByInvoiceNumber(invoice.getInvoiceNumber().getValue());
            invoice.getInvoiceLines().forEach(invoiceLine -> {
                請求データ明細Key key = new 請求データ明細Key();
                key.set請求番号(invoice.getInvoiceNumber().getValue());
                key.set売上番号(invoiceLine.getSalesNumber().getValue());
                key.set売上行番号(invoiceLine.getSalesLineNumber());
                請求データ明細 invoiceLineData = invoiceEntityMapper.mapToEntity(key);
                invoiceLineData.set作成日時(LocalDateTime.now());
                invoiceLineData.set作成者名(username);
                invoiceLineData.set更新日時(LocalDateTime.now());
                invoiceLineData.set更新者名(username);
                invoiceLineMapper.insert(invoiceLineData);
            });
        }
    }

    private void createInvoice(Invoice invoice, String username) {
        請求データ invoiceData = invoiceEntityMapper.mapToEntity(invoice);
        invoiceData.set作成日時(LocalDateTime.now());
        invoiceData.set作成者名(username);
        invoiceData.set更新日時(LocalDateTime.now());
        invoiceData.set更新者名(username);
        invoiceMapper.insert(invoiceData);

        if (invoice.getInvoiceLines() != null) {
            invoice.getInvoiceLines().forEach(invoiceLine -> {
                請求データ明細Key key = new 請求データ明細Key();
                key.set請求番号(invoice.getInvoiceNumber().getValue());
                key.set売上番号(invoiceLine.getSalesNumber().getValue());
                key.set売上行番号(invoiceLine.getSalesLineNumber());
                請求データ明細 invoiceLineData = invoiceEntityMapper.mapToEntity(key);
                invoiceLineData.set作成日時(LocalDateTime.now());
                invoiceLineData.set作成者名(username);
                invoiceLineData.set更新日時(LocalDateTime.now());
                invoiceLineData.set更新者名(username);
                invoiceLineMapper.insert(invoiceLineData);
            });
        }
    }

    @Override
    public InvoiceList selectAll() {
        List<InvoiceCustomEntity> invoiceCustomEntities = invoiceCustomMapper.selectAll();

        return new InvoiceList(invoiceCustomEntities.stream()
                .map(invoiceEntityMapper::mapToDomainModel)
                .toList()
        );
    }

    @Override
    public Optional<Invoice> findById(String invoiceId) {
        InvoiceCustomEntity invoiceCustomEntity = invoiceCustomMapper.selectByPrimaryKey(invoiceId);
        return invoiceCustomEntity != null ? Optional.of(invoiceEntityMapper.mapToDomainModel(invoiceCustomEntity)) : Optional.empty();
    }

    @Override
    public void delete(Invoice invoice) {
        if (!invoice.getInvoiceLines().isEmpty()) {
            invoice.getInvoiceLines().forEach(invoiceLine -> {
                請求データ明細Key key = new 請求データ明細Key();
                key.set請求番号(invoice.getInvoiceNumber().getValue());
                key.set売上番号(invoiceLine.getSalesNumber().getValue());
                key.set売上行番号(invoiceLine.getSalesLineNumber());
                invoiceLineMapper.deleteByPrimaryKey(key);
            });
        }
        invoiceMapper.deleteByPrimaryKey(invoice.getInvoiceNumber().getValue());
    }

    @Override
    public PageInfo<Invoice> selectAllWithPageInfo() {
        List<InvoiceCustomEntity> invoiceCustomEntities = invoiceCustomMapper.selectAll();
        PageInfo<InvoiceCustomEntity> pageInfo = new PageInfo<>(invoiceCustomEntities);
        return PageInfoHelper.of(pageInfo, invoiceEntityMapper::mapToDomainModel);
    }

    @Override
    public PageInfo<Invoice> searchWithPageInfo(InvoiceCriteria criteria) {
        List<InvoiceCustomEntity> invoiceCustomEntities = invoiceCustomMapper.selectByCriteria(criteria);
        PageInfo<InvoiceCustomEntity> pageInfo = new PageInfo<>(invoiceCustomEntities);
        return PageInfoHelper.of(pageInfo, invoiceEntityMapper::mapToDomainModel);
    }

    @Override
    public void save(InvoiceList invoiceList) {
        invoiceList.asList().forEach(this::save);
    }
}