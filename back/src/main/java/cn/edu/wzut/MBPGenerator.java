package cn.edu.wzut;

/**
 * @author zcz
 * @since 2022/7/2 16:19
 */
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class MBPGenerator {
    public static void main(String[] args) {
        //mysql连接信息
        String url = "jdbc:mysql://127.0.0.1:3306/test"; //MySQL的数据库连接
        String user = "root";  //用户名
        String password=  "123456"; //密码



        String baseDir = System.getProperty("user.dir"); //获取项目基目录
        String outputDir = baseDir + "//src//main//java//"; //添加maven规范的源代码目录

        FastAutoGenerator.create(url, user, password)
                .globalConfig(builder -> {
                    builder.author("zcz") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
//                            .fileOverride() // 覆盖已生成文件
                            .outputDir(outputDir); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("cn.edu.wzut") // 设置父包名
                            .moduleName("mbp"); // 设置父包模块名，可以为空
                })
                .templateConfig(builder -> {
                    builder.disable(TemplateType.CONTROLLER) //禁止产生控制器代码
                    ;
                })
                .strategyConfig(builder -> {
                    builder //.addInclude("sys_dept,sys_menu") // 设置需要生成的表名
                            .likeTable(new LikeTable("sys_")) //设置生成表的模糊匹配，查询%sys_%
                            //.addTablePrefix("sys_") // 或设置过滤表前缀，生成对象时去掉前缀
                            .entityBuilder().enableLombok() //启用lombok
                            .entityBuilder().superClass("cn.edu.wzut.mbp.entity.EntityBase") //设置entity的父类
                            .mapperBuilder().enableMapperAnnotation() //启用注解风格的mapper
                            .controllerBuilder().enableRestStyle() //启用rest风格
                    ;
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
