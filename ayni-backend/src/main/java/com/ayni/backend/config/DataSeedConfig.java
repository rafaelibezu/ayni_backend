package com.ayni.backend.config;

import com.ayni.backend.category.entity.Category;
import com.ayni.backend.category.repository.CategoryRepository;
import com.ayni.backend.common.enums.AuthProvider;
import com.ayni.backend.common.enums.RoleName;
import com.ayni.backend.product.entity.Product;
import com.ayni.backend.product.repository.ProductRepository;
import com.ayni.backend.role.entity.Role;
import com.ayni.backend.role.repository.RoleRepository;
import com.ayni.backend.user.entity.User;
import com.ayni.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeedConfig {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner loadInitialData() {
        return args -> {
            log.info("Verificando datos iniciales de la base de datos...");

            // 1. Crear Roles
            if (roleRepository.count() == 0) {
                Role adminRole = Role.builder()
                        .nombre(RoleName.ADMIN)
                        .descripcion("Administrador principal del sistema")
                        .creadoEn(LocalDateTime.now())
                        .build();

                Role userRole = Role.builder()
                        .nombre(RoleName.USER)
                        .descripcion("Cliente estándar de la tienda")
                        .creadoEn(LocalDateTime.now())
                        .build();

                roleRepository.saveAll(List.of(adminRole, userRole));
                log.info("✅ Roles iniciales creados.");
            }

            // 2. Crear Usuarios (2 Admins y 3 Clientes)
            if (userRepository.count() == 0) {
                Role adminRole = roleRepository.findAll().stream()
                        .filter(r -> r.getNombre().name().contains("ADMIN")).findFirst().orElseThrow();
                Role userRole = roleRepository.findAll().stream()
                        .filter(r -> r.getNombre().name().contains("USER")).findFirst().orElseThrow();

                // --- ADMINISTRADORES ---
                User admin1 = User.builder()
                        .nombres("Jhonnatan Gerardo")
                        .apellidos("Chavez del Castillo")
                        .email("admin@ayni.pe")
                        .passwordHash(passwordEncoder.encode("admin123"))
                        .proveedorAuth(AuthProvider.LOCAL)
                        .activo(true)
                        .emailVerificado(true)
                        .creadoEn(LocalDateTime.now())
                        .roles(Set.of(adminRole))
                        .build();

                User admin2 = User.builder()
                        .nombres("Rafael")
                        .apellidos("Iberico Ibezu")
                        .email("rafael@ayni.pe")
                        .passwordHash(passwordEncoder.encode("admin123"))
                        .proveedorAuth(AuthProvider.LOCAL)
                        .activo(true)
                        .emailVerificado(true)
                        .creadoEn(LocalDateTime.now())
                        .roles(Set.of(adminRole))
                        .build();

                // --- CLIENTES ---
                User cliente1 = User.builder()
                        .nombres("María")
                        .apellidos("González")
                        .email("maria@gmail.com")
                        .passwordHash(passwordEncoder.encode("user123"))
                        .proveedorAuth(AuthProvider.LOCAL)
                        .activo(true)
                        .emailVerificado(true)
                        .creadoEn(LocalDateTime.now())
                        .roles(Set.of(userRole))
                        .build();

                User cliente2 = User.builder()
                        .nombres("Carlos")
                        .apellidos("Sánchez")
                        .email("carlos@gmail.com")
                        .passwordHash(passwordEncoder.encode("user123"))
                        .proveedorAuth(AuthProvider.LOCAL)
                        .activo(true)
                        .emailVerificado(true)
                        .creadoEn(LocalDateTime.now())
                        .roles(Set.of(userRole))
                        .build();

                User cliente3 = User.builder()
                        .nombres("Lucía")
                        .apellidos("Mendoza")
                        .email("lucia@gmail.com")
                        .passwordHash(passwordEncoder.encode("user123"))
                        .proveedorAuth(AuthProvider.LOCAL)
                        .activo(true)
                        .emailVerificado(true)
                        .creadoEn(LocalDateTime.now())
                        .roles(Set.of(userRole))
                        .build();

                userRepository.saveAll(List.of(admin1, admin2, cliente1, cliente2, cliente3));
                log.info("✅ 2 Administradores y 3 Clientes creados.");
            }

            // 3. Crear Categorías (3 en total)
            if (categoryRepository.count() == 0) {
                Category belleza = Category.builder()
                        .nombre("Belleza")
                        .slug("belleza")
                        .descripcion("Productos curados para tu cuidado personal.")
                        .activo(true)
                        .creadoEn(LocalDateTime.now())
                        .build();

                Category hogar = Category.builder()
                        .nombre("Hogar")
                        .slug("hogar")
                        .descripcion("Artículos modernistas para elevar tu espacio.")
                        .activo(true)
                        .creadoEn(LocalDateTime.now())
                        .build();

                Category tecnologia = Category.builder()
                        .nombre("Tecnología")
                        .slug("tecnologia")
                        .descripcion("Innovación y diseño para tu día a día.")
                        .activo(true)
                        .creadoEn(LocalDateTime.now())
                        .build();

                categoryRepository.saveAll(List.of(belleza, hogar, tecnologia));
                log.info("✅ 3 Categorías iniciales creadas.");
            }

            // 4. Crear Productos (3 por categoría = 9 en total)
            if (productRepository.count() == 0) {
                Category catBelleza = categoryRepository.findAll().stream()
                        .filter(c -> c.getSlug().equals("belleza")).findFirst().orElseThrow();
                Category catHogar = categoryRepository.findAll().stream()
                        .filter(c -> c.getSlug().equals("hogar")).findFirst().orElseThrow();
                Category catTecnologia = categoryRepository.findAll().stream()
                        .filter(c -> c.getSlug().equals("tecnologia")).findFirst().orElseThrow();

                // --- PRODUCTOS BELLEZA ---
                Product belleza1 = Product.builder().category(catBelleza)
                        .nombre("Elixir Botánico de Resplandor").slug("elixir-botanico-resplandor")
                        .descripcion("50ML • ORIGEN ORGÁNICO").sku("BLLZ-001")
                        .precio(new BigDecimal("72.00")).stock(50).destacado(true).activo(true).creadoEn(LocalDateTime.now()).build();

                Product belleza2 = Product.builder().category(catBelleza)
                        .nombre("Suero Facial Hidratante").slug("suero-facial-hidratante")
                        .descripcion("Con Ácido Hialurónico y Vitamina C").sku("BLLZ-002")
                        .precio(new BigDecimal("55.00")).stock(40).destacado(false).activo(true).creadoEn(LocalDateTime.now()).build();

                Product belleza3 = Product.builder().category(catBelleza)
                        .nombre("Mascarilla de Arcilla Purificante").slug("mascarilla-arcilla-purificante")
                        .descripcion("Limpieza profunda para todo tipo de piel").sku("BLLZ-003")
                        .precio(new BigDecimal("30.00")).stock(80).destacado(false).activo(true).creadoEn(LocalDateTime.now()).build();

                // --- PRODUCTOS HOGAR ---
                Product hogar1 = Product.builder().category(catHogar)
                        .nombre("Vela de Cerámica Bosque de Medianoche").slug("vela-ceramica-bosque")
                        .descripcion("VERTIDA A MANO • SÁNDALO Y CEDRO").sku("HGR-001")
                        .precio(new BigDecimal("48.00")).stock(35).destacado(true).activo(true).creadoEn(LocalDateTime.now()).build();

                Product hogar2 = Product.builder().category(catHogar)
                        .nombre("Lámpara de Mesa Minimalista").slug("lampara-mesa-minimalista")
                        .descripcion("Luz cálida regulable, diseño nórdico").sku("HGR-002")
                        .precio(new BigDecimal("120.00")).stock(15).destacado(true).activo(true).creadoEn(LocalDateTime.now()).build();

                Product hogar3 = Product.builder().category(catHogar)
                        .nombre("Manta Tejida de Algodón").slug("manta-tejida-algodon")
                        .descripcion("100% Algodón orgánico, textura suave").sku("HGR-003")
                        .precio(new BigDecimal("85.00")).stock(25).destacado(false).activo(true).creadoEn(LocalDateTime.now()).build();

                // --- PRODUCTOS TECNOLOGÍA ---
                Product tech1 = Product.builder().category(catTecnologia)
                        .nombre("Auriculares Inalámbricos Pro").slug("auriculares-inalambricos-pro")
                        .descripcion("Cancelación de ruido activa, 24h de batería").sku("TEC-001")
                        .precio(new BigDecimal("250.00")).stock(20).destacado(true).activo(true).creadoEn(LocalDateTime.now()).build();

                Product tech2 = Product.builder().category(catTecnologia)
                        .nombre("Altavoz Bluetooth Inteligente").slug("altavoz-bluetooth-inteligente")
                        .descripcion("Sonido 360 y asistente de voz integrado").sku("TEC-002")
                        .precio(new BigDecimal("150.00")).stock(30).destacado(false).activo(true).creadoEn(LocalDateTime.now()).build();

                Product tech3 = Product.builder().category(catTecnologia)
                        .nombre("Estación de Carga Inalámbrica").slug("estacion-carga-inalambrica")
                        .descripcion("Carga rápida para 3 dispositivos simultáneos").sku("TEC-003")
                        .precio(new BigDecimal("45.00")).stock(60).destacado(false).activo(true).creadoEn(LocalDateTime.now()).build();

                productRepository.saveAll(List.of(belleza1, belleza2, belleza3, hogar1, hogar2, hogar3, tech1, tech2, tech3));
                log.info("✅ 9 Productos de prueba inyectados al catálogo.");
            }

            log.info("🚀 ¡Inyección de datos masiva finalizada con éxito!");
        };
    }
}