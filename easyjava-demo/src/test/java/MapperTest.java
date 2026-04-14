import com.easyjava.RunDemoApplication;
import com.easyjava.entity.po.ProductInfo;
import com.easyjava.entity.query.ProductInfoQuery;
import com.easyjava.entity.vo.PaginationResultVO;
import com.easyjava.mappers.ProductInfoMapper;
import com.easyjava.service.ProductInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest(classes = RunDemoApplication.class)
public class MapperTest {

    @Autowired
    private ProductInfoMapper<ProductInfo, ProductInfoQuery> productInfoMapper;

    @Autowired
    private ProductInfoService productInfoService;

    private static final Random random = new Random();

    /**
     * 生成随机 code，避免唯一索引冲突
     */
    private String randomCode() {
        return "T" + System.currentTimeMillis() % 100000 + random.nextInt(1000);
    }

    /**
     * 生成随机 skuType 和 colorType 组合，避免唯一索引冲突
     * 数据库字段是 tinyint，范围 -128 到 127
     */
    private int[] randomSkuColor() {
        int sku = random.nextInt(100) + 1;  // 1-100
        int color = random.nextInt(100) + 1;  // 1-100
        return new int[]{sku, color};
    }

    // ==================== BaseMapper 方法测试 ====================

    /**
     * 测试 selectList - 查询列表
     */
    @Test
    public void testSelectList() {
        System.out.println("========== 测试 selectList ==========");
        ProductInfoQuery query = new ProductInfoQuery();
        query.setCodeFuzzy("5");
        List<ProductInfo> list = productInfoMapper.selectList(query);
        System.out.println("查询结果数量: " + list.size());
        for (ProductInfo productInfo : list) {
            System.out.println(productInfo);
        }
        System.out.println("========== selectList 测试完成 ==========\n");
    }

    /**
     * 测试 selectCount - 查询数量
     */
    @Test
    public void testSelectCount() {
        System.out.println("========== 测试 selectCount ==========");
        ProductInfoQuery query = new ProductInfoQuery();
        query.setCodeFuzzy("5");
        Integer count = productInfoMapper.selectCount(query);
        System.out.println("查询数量: " + count);
        System.out.println("========== selectCount 测试完成 ==========\n");
    }

    /**
     * 测试 insert - 单条插入
     */
    @Test
    public void testInsert() {
        System.out.println("========== 测试 insert ==========");
        int[] skuColor = randomSkuColor();
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(randomCode());
        productInfo.setCompanyId("C001");
        productInfo.setProductName("测试商品-INSERT");
        productInfo.setPrice(BigDecimal.valueOf(999.99));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(100L);
        productInfo.setStatus(1);

        Integer result = productInfoMapper.insert(productInfo);
        System.out.println("插入结果: " + result + ", 生成ID: " + productInfo.getId());
        System.out.println("========== insert 测试完成 ==========\n");
    }

    /**
     * 测试 insertOrUpdate - 插入或更新
     */
    @Test
    public void testInsertOrUpdate() {
        System.out.println("========== 测试 insertOrUpdate ==========");
        int[] skuColor = randomSkuColor();
        String code = randomCode();

        // 测试插入新记录
        ProductInfo newProduct = new ProductInfo();
        newProduct.setCode(code);
        newProduct.setCompanyId("C001");
        newProduct.setProductName("测试商品-INSERTORUPDATE-NEW");
        newProduct.setPrice(BigDecimal.valueOf(1999.00));
        newProduct.setSkuType(skuColor[0]);
        newProduct.setColorType(skuColor[1]);
        newProduct.setStock(200L);
        newProduct.setStatus(1);

        Integer insertResult = productInfoMapper.insertOrUpdate(newProduct);
        System.out.println("插入结果: " + insertResult + ", 新ID: " + newProduct.getId());

        // 测试更新已有记录
        ProductInfo existingProduct = new ProductInfo();
        existingProduct.setCode(code);
        existingProduct.setCompanyId("C001");
        existingProduct.setProductName("测试商品-INSERTORUPDATE-UPDATED");
        existingProduct.setPrice(BigDecimal.valueOf(2999.00));
        existingProduct.setSkuType(skuColor[0]);
        existingProduct.setColorType(skuColor[1]);
        existingProduct.setStock(300L);
        existingProduct.setStatus(2);

        Integer updateResult = productInfoMapper.insertOrUpdate(existingProduct);
        System.out.println("更新结果: " + updateResult);
        System.out.println("========== insertOrUpdate 测试完成 ==========\n");
    }

    /**
     * 测试 insertBatch - 批量插入
     */
    @Test
    public void testInsertBatch() {
        System.out.println("========== 测试 insertBatch ==========");
        List<ProductInfo> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            int[] skuColor = randomSkuColor();
            ProductInfo productInfo = new ProductInfo();
            productInfo.setCode(randomCode());
            productInfo.setCompanyId("C002");
            productInfo.setProductName("测试商品-BATCH-" + i);
            productInfo.setPrice(BigDecimal.valueOf(100 + i));
            productInfo.setSkuType(skuColor[0]);
            productInfo.setColorType(skuColor[1]);
            productInfo.setStock(50L);
            productInfo.setStatus(1);
            list.add(productInfo);
        }

        Integer result = productInfoMapper.insertBatch(list);
        System.out.println("批量插入结果: " + result);
        System.out.println("========== insertBatch 测试完成 ==========\n");
    }

    /**
     * 测试 insertOrUpdateBatch - 批量插入或更新
     */
    @Test
    public void testInsertOrUpdateBatch() {
        System.out.println("========== 测试 insertOrUpdateBatch ==========");
        List<ProductInfo> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            int[] skuColor = randomSkuColor();
            ProductInfo productInfo = new ProductInfo();
            productInfo.setCode(randomCode());
            productInfo.setCompanyId("C003");
            productInfo.setProductName("测试商品-BATCHUPSERT-" + i);
            productInfo.setPrice(BigDecimal.valueOf(200 + i));
            productInfo.setSkuType(skuColor[0]);
            productInfo.setColorType(skuColor[1]);
            productInfo.setStock(100L);
            productInfo.setStatus(1);
            list.add(productInfo);
        }

        Integer result = productInfoMapper.insertOrUpdateBatch(list);
        System.out.println("批量插入或更新结果: " + result);
        System.out.println("========== insertOrUpdateBatch 测试完成 ==========\n");
    }

    // ==================== ProductInfoMapper 方法测试 ====================

    /**
     * 测试 selectById - 根据Id查询
     */
    @Test
    public void testSelectById() {
        System.out.println("========== 测试 selectById ==========");
        int[] skuColor = randomSkuColor();
        // 先插入一条数据
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(randomCode());
        productInfo.setCompanyId("C004");
        productInfo.setProductName("测试商品-SELECTBYID");
        productInfo.setPrice(BigDecimal.valueOf(500.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoMapper.insert(productInfo);

        Integer id = productInfo.getId();
        System.out.println("插入数据ID: " + id);

        // 查询
        ProductInfo result = productInfoMapper.selectById(id);
        System.out.println("查询结果: " + result);
        System.out.println("========== selectById 测试完成 ==========\n");
    }

    /**
     * 测试 updateById - 根据Id更新
     */
    @Test
    public void testUpdateById() {
        System.out.println("========== 测试 updateById ==========");
        int[] skuColor = randomSkuColor();
        // 先插入一条数据
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(randomCode());
        productInfo.setCompanyId("C005");
        productInfo.setProductName("测试商品-UPDATEBYID-BEFORE");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoMapper.insert(productInfo);

        Integer id = productInfo.getId();
        System.out.println("插入数据ID: " + id);

        // 更新
        ProductInfo updateBean = new ProductInfo();
        updateBean.setProductName("测试商品-UPDATEBYID-AFTER");
        updateBean.setPrice(BigDecimal.valueOf(999.00));
        updateBean.setStock(999L);

        Integer result = productInfoMapper.updateById(updateBean, id);
        System.out.println("更新影响行数: " + result);

        // 验证更新
        ProductInfo updated = productInfoMapper.selectById(id);
        System.out.println("更新后数据: " + updated);
        System.out.println("========== updateById 测试完成 ==========\n");
    }

    /**
     * 测试 deleteById - 根据Id删除
     */
    @Test
    public void testDeleteById() {
        System.out.println("========== 测试 deleteById ==========");
        int[] skuColor = randomSkuColor();
        // 先插入一条数据
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(randomCode());
        productInfo.setCompanyId("C006");
        productInfo.setProductName("测试商品-DELETEBYID");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoMapper.insert(productInfo);

        Integer id = productInfo.getId();
        System.out.println("插入数据ID: " + id);

        // 删除
        Integer result = productInfoMapper.deleteById(id);
        System.out.println("删除影响行数: " + result);

        // 验证删除
        ProductInfo deleted = productInfoMapper.selectById(id);
        System.out.println("删除后查询结果: " + deleted);
        System.out.println("========== deleteById 测试完成 ==========\n");
    }

    /**
     * 测试 selectBySkuTypeAndColorType - 根据SkuTypeAndColorType查询
     */
    @Test
    public void testSelectBySkuTypeAndColorType() {
        System.out.println("========== 测试 selectBySkuTypeAndColorType ==========");
        int[] skuColor = randomSkuColor();
        // 先插入测试数据
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(randomCode());
        productInfo.setCompanyId("C007");
        productInfo.setProductName("测试商品-SKUCOLOR");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoMapper.insert(productInfo);

        // 查询
        ProductInfo result = productInfoMapper.selectBySkuTypeAndColorType(skuColor[0], skuColor[1]);
        System.out.println("查询结果: " + result);
        System.out.println("========== selectBySkuTypeAndColorType 测试完成 ==========\n");
    }

    /**
     * 测试 updateBySkuTypeAndColorType - 根据SkuTypeAndColorType更新
     */
    @Test
    public void testUpdateBySkuTypeAndColorType() {
        System.out.println("========== 测试 updateBySkuTypeAndColorType ==========");
        int[] skuColor = randomSkuColor();
        // 先插入测试数据
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(randomCode());
        productInfo.setCompanyId("C008");
        productInfo.setProductName("测试商品-SKUCOLOR-UPDATE-BEFORE");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoMapper.insert(productInfo);

        // 更新
        ProductInfo updateBean = new ProductInfo();
        updateBean.setProductName("测试商品-SKUCOLOR-UPDATE-AFTER");
        updateBean.setPrice(BigDecimal.valueOf(888.00));
        updateBean.setStock(888L);

        Integer result = productInfoMapper.updateBySkuTypeAndColorType(updateBean, skuColor[0], skuColor[1]);
        System.out.println("更新影响行数: " + result);

        // 验证更新
        ProductInfo updated = productInfoMapper.selectBySkuTypeAndColorType(skuColor[0], skuColor[1]);
        System.out.println("更新后数据: " + updated);
        System.out.println("========== updateBySkuTypeAndColorType 测试完成 ==========\n");
    }

    /**
     * 测试 deleteBySkuTypeAndColorType - 根据SkuTypeAndColorType删除
     */
    @Test
    public void testDeleteBySkuTypeAndColorType() {
        System.out.println("========== 测试 deleteBySkuTypeAndColorType ==========");
        int[] skuColor = randomSkuColor();
        // 先插入测试数据
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(randomCode());
        productInfo.setCompanyId("C009");
        productInfo.setProductName("测试商品-SKUCOLOR-DELETE");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoMapper.insert(productInfo);

        // 删除
        Integer result = productInfoMapper.deleteBySkuTypeAndColorType(skuColor[0], skuColor[1]);
        System.out.println("删除影响行数: " + result);

        // 验证删除
        ProductInfo deleted = productInfoMapper.selectBySkuTypeAndColorType(skuColor[0], skuColor[1]);
        System.out.println("删除后查询结果: " + deleted);
        System.out.println("========== deleteBySkuTypeAndColorType 测试完成 ==========\n");
    }

    /**
     * 测试 selectByCode - 根据Code查询
     */
    @Test
    public void testSelectByCode() {
        System.out.println("========== 测试 selectByCode ==========");
        int[] skuColor = randomSkuColor();
        String code = randomCode();
        // 先插入测试数据
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(code);
        productInfo.setCompanyId("C010");
        productInfo.setProductName("测试商品-CODE");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoMapper.insert(productInfo);

        // 查询
        ProductInfo result = productInfoMapper.selectByCode(code);
        System.out.println("查询结果: " + result);
        System.out.println("========== selectByCode 测试完成 ==========\n");
    }

    /**
     * 测试 updateByCode - 根据Code更新
     */
    @Test
    public void testUpdateByCode() {
        System.out.println("========== 测试 updateByCode ==========");
        int[] skuColor = randomSkuColor();
        String code = randomCode();
        // 先插入测试数据
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(code);
        productInfo.setCompanyId("C011");
        productInfo.setProductName("测试商品-CODE-UPDATE-BEFORE");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoMapper.insert(productInfo);

        // 更新
        ProductInfo updateBean = new ProductInfo();
        updateBean.setProductName("测试商品-CODE-UPDATE-AFTER");
        updateBean.setPrice(BigDecimal.valueOf(777.00));
        updateBean.setStock(777L);

        Integer result = productInfoMapper.updateByCode(updateBean, code);
        System.out.println("更新影响行数: " + result);

        // 验证更新
        ProductInfo updated = productInfoMapper.selectByCode(code);
        System.out.println("更新后数据: " + updated);
        System.out.println("========== updateByCode 测试完成 ==========\n");
    }

    /**
     * 测试 deleteByCode - 根据Code删除
     */
    @Test
    public void testDeleteByCode() {
        System.out.println("========== 测试 deleteByCode ==========");
        int[] skuColor = randomSkuColor();
        String code = randomCode();
        // 先插入测试数据
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(code);
        productInfo.setCompanyId("C012");
        productInfo.setProductName("测试商品-CODE-DELETE");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoMapper.insert(productInfo);

        // 删除
        Integer result = productInfoMapper.deleteByCode(code);
        System.out.println("删除影响行数: " + result);

        // 验证删除
        ProductInfo deleted = productInfoMapper.selectByCode(code);
        System.out.println("删除后查询结果: " + deleted);
        System.out.println("========== deleteByCode 测试完成 ==========\n");
    }

    // ==================== Query 条件测试 ====================

    /**
     * 测试基础查询条件
     */
    @Test
    public void testQueryConditions() {
        System.out.println("========== 测试 Query 条件 ==========");

        // 测试精确查询
        ProductInfoQuery query1 = new ProductInfoQuery();
        query1.setId(1);
        List<ProductInfo> list1 = productInfoMapper.selectList(query1);
        System.out.println("ID精确查询结果数量: " + list1.size());

        // 测试模糊查询
        ProductInfoQuery query2 = new ProductInfoQuery();
        query2.setCodeFuzzy("P");
        List<ProductInfo> list2 = productInfoMapper.selectList(query2);
        System.out.println("Code模糊查询结果数量: " + list2.size());

        // 测试多条件组合
        ProductInfoQuery query3 = new ProductInfoQuery();
        query3.setCompanyId("C001");
        query3.setSkuType(1);
        List<ProductInfo> list3 = productInfoMapper.selectList(query3);
        System.out.println("多条件组合查询结果数量: " + list3.size());

        System.out.println("========== Query 条件测试完成 ==========\n");
    }

    /**
     * 测试时间范围查询
     */
    @Test
    public void testTimeRangeQuery() {
        System.out.println("========== 测试时间范围查询 ==========");

        ProductInfoQuery query = new ProductInfoQuery();
        query.setCreateTimeStart("2024-01-01");
        query.setCreateTimeEnd("2026-12-31");

        List<ProductInfo> list = productInfoMapper.selectList(query);
        System.out.println("时间范围查询结果数量: " + list.size());

        System.out.println("========== 时间范围查询测试完成 ==========\n");
    }

    // ==================== Service 方法测试 ====================

    /**
     * 测试 Service - findListByParam
     */
    @Test
    public void testServiceFindListByParam() {
        System.out.println("========== 测试 Service findListByParam ==========");
        ProductInfoQuery query = new ProductInfoQuery();
        query.setCodeFuzzy("T");
        List<ProductInfo> list = productInfoService.findListByParam(query);
        System.out.println("查询结果数量: " + list.size());
        System.out.println("========== findListByParam 测试完成 ==========\n");
    }

    /**
     * 测试 Service - findCountByParam
     */
    @Test
    public void testServiceFindCountByParam() {
        System.out.println("========== 测试 Service findCountByParam ==========");
        ProductInfoQuery query = new ProductInfoQuery();
        query.setCodeFuzzy("T");
        Integer count = productInfoService.findCountByParam(query);
        System.out.println("查询数量: " + count);
        System.out.println("========== findCountByParam 测试完成 ==========\n");
    }

    /**
     * 测试 Service - findListByPage
     */
    @Test
    public void testServiceFindListByPage() {
        System.out.println("========== 测试 Service findListByPage ==========");
        ProductInfoQuery query = new ProductInfoQuery();
        query.setPageNo(1);
        query.setPageSize(5);
        PaginationResultVO<ProductInfo> result = productInfoService.findListByPage(query);
        System.out.println("总数: " + result.getTotalCount());
        System.out.println("页码: " + result.getPageNo());
        System.out.println("每页数量: " + result.getPageSize());
        System.out.println("总页数: " + result.getPageTotal());
        System.out.println("当前页数据量: " + result.getList().size());
        System.out.println("========== findListByPage 测试完成 ==========\n");
    }

    /**
     * 测试 Service - add
     */
    @Test
    public void testServiceAdd() {
        System.out.println("========== 测试 Service add ==========");
        int[] skuColor = randomSkuColor();
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(randomCode());
        productInfo.setCompanyId("C_SERVICE");
        productInfo.setProductName("Service测试-ADD");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);

        Integer result = productInfoService.add(productInfo);
        System.out.println("新增结果: " + result + ", ID: " + productInfo.getId());
        System.out.println("========== add 测试完成 ==========\n");
    }

    /**
     * 测试 Service - addBatch
     */
    @Test
    public void testServiceAddBatch() {
        System.out.println("========== 测试 Service addBatch ==========");
        List<ProductInfo> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int[] skuColor = randomSkuColor();
            ProductInfo productInfo = new ProductInfo();
            productInfo.setCode(randomCode());
            productInfo.setCompanyId("C_BATCH");
            productInfo.setProductName("Service测试-BATCH-" + i);
            productInfo.setPrice(BigDecimal.valueOf(100 + i));
            productInfo.setSkuType(skuColor[0]);
            productInfo.setColorType(skuColor[1]);
            productInfo.setStock(10L);
            productInfo.setStatus(1);
            list.add(productInfo);
        }

        Integer result = productInfoService.addBatch(list);
        System.out.println("批量新增结果: " + result);
        System.out.println("========== addBatch 测试完成 ==========\n");
    }

    /**
     * 测试 Service - insertOrUpdateBatch
     */
    @Test
    public void testServiceInsertOrUpdateBatch() {
        System.out.println("========== 测试 Service insertOrUpdateBatch ==========");
        List<ProductInfo> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int[] skuColor = randomSkuColor();
            ProductInfo productInfo = new ProductInfo();
            productInfo.setCode(randomCode());
            productInfo.setCompanyId("C_UPSERT");
            productInfo.setProductName("Service测试-UPSERT-" + i);
            productInfo.setPrice(BigDecimal.valueOf(200 + i));
            productInfo.setSkuType(skuColor[0]);
            productInfo.setColorType(skuColor[1]);
            productInfo.setStock(20L);
            productInfo.setStatus(1);
            list.add(productInfo);
        }

        Integer result = productInfoService.insertOrUpdateBatch(list);
        System.out.println("批量新增或更新结果: " + result);
        System.out.println("========== insertOrUpdateBatch 测试完成 ==========\n");
    }

    /**
     * 测试 Service - getProductInfoById
     */
    @Test
    public void testServiceGetProductInfoById() {
        System.out.println("========== 测试 Service getProductInfoById ==========");
        int[] skuColor = randomSkuColor();
        // 先插入
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(randomCode());
        productInfo.setCompanyId("C_ID");
        productInfo.setProductName("Service测试-ByID");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoService.add(productInfo);

        // 查询
        ProductInfo result = productInfoService.getProductInfoById(productInfo.getId());
        System.out.println("查询结果: " + result);
        System.out.println("========== getProductInfoById 测试完成 ==========\n");
    }

    /**
     * 测试 Service - updateProductInfoById
     */
    @Test
    public void testServiceUpdateProductInfoById() {
        System.out.println("========== 测试 Service updateProductInfoById ==========");
        int[] skuColor = randomSkuColor();
        // 先插入
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(randomCode());
        productInfo.setCompanyId("C_UPD");
        productInfo.setProductName("Service测试-Update-BEFORE");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoService.add(productInfo);

        // 更新
        ProductInfo updateBean = new ProductInfo();
        updateBean.setProductName("Service测试-Update-AFTER");
        updateBean.setPrice(BigDecimal.valueOf(999.00));
        updateBean.setStock(999L);

        Integer result = productInfoService.updateProductInfoById(updateBean, productInfo.getId());
        System.out.println("更新影响行数: " + result);

        // 验证
        ProductInfo updated = productInfoService.getProductInfoById(productInfo.getId());
        System.out.println("更新后数据: " + updated);
        System.out.println("========== updateProductInfoById 测试完成 ==========\n");
    }

    /**
     * 测试 Service - deleteProductInfoById
     */
    @Test
    public void testServiceDeleteProductInfoById() {
        System.out.println("========== 测试 Service deleteProductInfoById ==========");
        int[] skuColor = randomSkuColor();
        // 先插入
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(randomCode());
        productInfo.setCompanyId("C_DEL");
        productInfo.setProductName("Service测试-Delete");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoService.add(productInfo);

        // 删除
        Integer result = productInfoService.deleteProductInfoById(productInfo.getId());
        System.out.println("删除影响行数: " + result);

        // 验证
        ProductInfo deleted = productInfoService.getProductInfoById(productInfo.getId());
        System.out.println("删除后查询结果: " + deleted);
        System.out.println("========== deleteProductInfoById 测试完成 ==========\n");
    }

    /**
     * 测试 Service - getProductInfoBySkuTypeAndColorType
     */
    @Test
    public void testServiceGetProductInfoBySkuTypeAndColorType() {
        System.out.println("========== 测试 Service getProductInfoBySkuTypeAndColorType ==========");
        int[] skuColor = randomSkuColor();
        // 先插入
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(randomCode());
        productInfo.setCompanyId("C_SKU");
        productInfo.setProductName("Service测试-SkuColor");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoService.add(productInfo);

        // 查询
        ProductInfo result = productInfoService.getProductInfoBySkuTypeAndColorType(skuColor[0], skuColor[1]);
        System.out.println("查询结果: " + result);
        System.out.println("========== getProductInfoBySkuTypeAndColorType 测试完成 ==========\n");
    }

    /**
     * 测试 Service - updateProductInfoBySkuTypeAndColorType
     */
    @Test
    public void testServiceUpdateProductInfoBySkuTypeAndColorType() {
        System.out.println("========== 测试 Service updateProductInfoBySkuTypeAndColorType ==========");
        int[] skuColor = randomSkuColor();
        // 先插入
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(randomCode());
        productInfo.setCompanyId("C_SKUUPD");
        productInfo.setProductName("Service测试-SkuColor-BEFORE");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoService.add(productInfo);

        // 更新
        ProductInfo updateBean = new ProductInfo();
        updateBean.setProductName("Service测试-SkuColor-AFTER");
        updateBean.setPrice(BigDecimal.valueOf(888.00));
        updateBean.setStock(888L);

        Integer result = productInfoService.updateProductInfoBySkuTypeAndColorType(updateBean, skuColor[0], skuColor[1]);
        System.out.println("更新影响行数: " + result);

        // 验证
        ProductInfo updated = productInfoService.getProductInfoBySkuTypeAndColorType(skuColor[0], skuColor[1]);
        System.out.println("更新后数据: " + updated);
        System.out.println("========== updateProductInfoBySkuTypeAndColorType 测试完成 ==========\n");
    }

    /**
     * 测试 Service - deleteProductInfoBySkuTypeAndColorType
     */
    @Test
    public void testServiceDeleteProductInfoBySkuTypeAndColorType() {
        System.out.println("========== 测试 Service deleteProductInfoBySkuTypeAndColorType ==========");
        int[] skuColor = randomSkuColor();
        // 先插入
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(randomCode());
        productInfo.setCompanyId("C_SKUDEL");
        productInfo.setProductName("Service测试-SkuColor-DEL");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoService.add(productInfo);

        // 删除
        Integer result = productInfoService.deleteProductInfoBySkuTypeAndColorType(skuColor[0], skuColor[1]);
        System.out.println("删除影响行数: " + result);

        // 验证
        ProductInfo deleted = productInfoService.getProductInfoBySkuTypeAndColorType(skuColor[0], skuColor[1]);
        System.out.println("删除后查询结果: " + deleted);
        System.out.println("========== deleteProductInfoBySkuTypeAndColorType 测试完成 ==========\n");
    }

    /**
     * 测试 Service - getProductInfoByCode
     */
    @Test
    public void testServiceGetProductInfoByCode() {
        System.out.println("========== 测试 Service getProductInfoByCode ==========");
        int[] skuColor = randomSkuColor();
        String code = randomCode();
        // 先插入
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(code);
        productInfo.setCompanyId("C_CODE");
        productInfo.setProductName("Service测试-Code");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoService.add(productInfo);

        // 查询
        ProductInfo result = productInfoService.getProductInfoByCode(code);
        System.out.println("查询结果: " + result);
        System.out.println("========== getProductInfoByCode 测试完成 ==========\n");
    }

    /**
     * 测试 Service - updateProductInfoByCode
     */
    @Test
    public void testServiceUpdateProductInfoByCode() {
        System.out.println("========== 测试 Service updateProductInfoByCode ==========");
        int[] skuColor = randomSkuColor();
        String code = randomCode();
        // 先插入
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(code);
        productInfo.setCompanyId("C_CODEUPD");
        productInfo.setProductName("Service测试-Code-BEFORE");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoService.add(productInfo);

        // 更新
        ProductInfo updateBean = new ProductInfo();
        updateBean.setProductName("Service测试-Code-AFTER");
        updateBean.setPrice(BigDecimal.valueOf(777.00));
        updateBean.setStock(777L);

        Integer result = productInfoService.updateProductInfoByCode(updateBean, code);
        System.out.println("更新影响行数: " + result);

        // 验证
        ProductInfo updated = productInfoService.getProductInfoByCode(code);
        System.out.println("更新后数据: " + updated);
        System.out.println("========== updateProductInfoByCode 测试完成 ==========\n");
    }

    /**
     * 测试 Service - deleteProductInfoByCode
     */
    @Test
    public void testServiceDeleteProductInfoByCode() {
        System.out.println("========== 测试 Service deleteProductInfoByCode ==========");
        int[] skuColor = randomSkuColor();
        String code = randomCode();
        // 先插入
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(code);
        productInfo.setCompanyId("C_CODEDEL");
        productInfo.setProductName("Service测试-Code-DEL");
        productInfo.setPrice(BigDecimal.valueOf(100.00));
        productInfo.setSkuType(skuColor[0]);
        productInfo.setColorType(skuColor[1]);
        productInfo.setStock(10L);
        productInfo.setStatus(1);
        productInfoService.add(productInfo);

        // 删除
        Integer result = productInfoService.deleteProductInfoByCode(code);
        System.out.println("删除影响行数: " + result);

        // 验证
        ProductInfo deleted = productInfoService.getProductInfoByCode(code);
        System.out.println("删除后查询结果: " + deleted);
        System.out.println("========== deleteProductInfoByCode 测试完成 ==========\n");
    }
}
