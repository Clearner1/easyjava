import com.easyjava.RunDemoApplication;
import com.easyjava.entity.po.ProductInfo;
import com.easyjava.entity.query.ProductInfoQuery;
import com.easyjava.mappers.ProductInfoMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = RunDemoApplication.class)
public class MapperTest {

    @Autowired
    private ProductInfoMapper<ProductInfo, ProductInfoQuery> productInfoMapper;

    @Test
    public void selectList() {
        List list = productInfoMapper.selectList(new ProductInfoQuery());
        System.out.println(list.size());
    }

    @Test
    public void main() {
        System.out.println("1");
    }
}
