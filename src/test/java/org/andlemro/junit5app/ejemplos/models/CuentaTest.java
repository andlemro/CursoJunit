package org.andlemro.junit5app.ejemplos.models;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.adlemro.junit5app.ejemplos.exceptions.DineroInsuficienteException;
import org.adlemro.junit5app.ejemplos.models.Banco;
import org.adlemro.junit5app.ejemplos.models.Cuenta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class CuentaTest {
	
	Cuenta cuenta;
	
	private TestInfo testInfo;
	private TestReporter testReporter;
	
	// Con la expresion @BeforeEach indicamos que este metodo se va a ejecutar al inicio de cada instancia de cada metodo test
	@BeforeEach
	void initMetodoTest(TestInfo testInfo, TestReporter testReporter) {
		this.cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
		System.out.println("Iniciando el metodo.");
		
		this.testInfo = testInfo;
		this.testReporter = testReporter;
		testReporter.publishEntry(" Ejecutando: " + testInfo.getDisplayName() + " " + testInfo.getTestMethod().get().getName()
				+ " con las etiquetas " + testInfo.getTags());
	}
	
	// Con la expresion @AfterEach indicamos que este metodo se va a ejecutar al finalizar cada instancia de cada metodo test
	@AfterEach
	void tearDown() {
		System.out.println("Finalizando el metodo de prueba.");
	}
	
	// Con la expresion @BeforeAll indicamos que este metodo se va a ejecutar al inicio, antes de ejecutar cada metodo test, solo se ejecuta una vez.
	@BeforeAll
	static void beforeAll() {
		System.out.println("Inicializando el test.");
	}
	
	// Con la expresion @AfterAll indicamos que este metodo se va a ejecutar al final, despues de ejecutar cada metodo test, solo se ejecuta una vez.
	@AfterAll
	static void afeterAll() {
		System.out.println("Finalizando el test.");
	}
	
	// Con la anotacion Nested indicamos que vamos a ejecutar una clase anidada
	// Con la etiqueta Tag especificamos que pruebas son las que queremos ejecutar, de acuerdo a la configuracion de run configuration.
	@Tag("cuenta")
	@Nested
	@DisplayName("Probando atributos de la cuenta.")
	class CuentaTestNombreSaldo {
		@Test
		@DisplayName("El nombre.")
		void testNombreCuenta() {
//			cuenta.setPersona("Andres");
			
			testReporter.publishEntry(testInfo.getTags().toString());
			if(testInfo.getTags().contains("cuenta")) {
				testReporter.publishEntry("Hacer algo con la etiqueta cuenta");
			}

			String esperado = "Andres";
			String real = cuenta.getPersona();
			
			/** 
			 * con la exprecion lambda (arrow functions) nos permite instanciar los strings
			 * solo cuando se ejecuta en el momento del error, si todo sale correcto, no se instancian
			 *  por lo cual es mas eficiente al momento de ejecucion de la aplicacion.
			**/
			assertNotNull(real, () -> "La cuenta no puede ser nula");
			assertEquals(esperado, real, () -> "El nombre de la cuenta no es el que se  esperaba");
			assertTrue(real.equals("Andres"), () -> "Nombre cuenta esperada  debe ser igual a la real");

//			fail("Not yet implemented");
		}
		
		@Test
		@DisplayName("El saldo, que no sea null, mayor que cero, valor esperado.")
		void testSaldoCuenta() {
			assertNotNull(cuenta.getSaldo());
			assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
			assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0); 
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); 
		}
		
		@Test
		@DisplayName("Testeando referencias que sean iguales con el metodo equals.")
		void testReferenciaCuenta( ) {
			cuenta = new Cuenta("Jhon Doe", new BigDecimal("8900.9997"));
			Cuenta cuenta2 = new Cuenta("Jhon Doe", new BigDecimal("8900.9997"));
			
//			assertNotEquals(cuenta2, cuenta);
			assertEquals(cuenta2, cuenta);
		}
	}
 
	
	// Con la anotacion Nested indicamos que vamos a ejecutar una clase anidada
	@Nested
	class CuentaOperacionesTest {
		
		// Con la etiqueta Tag especificamos que pruebas son las que queremos ejecutar, de acuerdo a la configuracion de run configuration.
		@Tag("cuenta")
		@Test
		void testDebitoCuenta( ) {
			cuenta.debito(new BigDecimal(100));
			assertNotNull(cuenta.getSaldo());
			assertEquals(900, cuenta.getSaldo().intValue());
			assertEquals("900.12345", cuenta.getSaldo().toPlainString());
		}
		
		@Tag("cuenta")
		@Test
		void testCreditoCuenta( ) {
			cuenta.credito(new BigDecimal(100));
			assertNotNull(cuenta.getSaldo());
			assertEquals(1100, cuenta.getSaldo().intValue());
			assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
		}
		
		@Tag("cuenta")
		@Tag("banco")
		@Test
		void testTransferirDineroCuentas() {
			Cuenta cuentaDestino = new Cuenta("Jhon Doe", new BigDecimal("2500"));
			Cuenta cuentaOrigen = new Cuenta("Andres", new BigDecimal("1500.8989"));
			
			Banco banco = new Banco();
			banco.setNombre("Banco del Estado");
			banco.transferir(cuentaOrigen, cuentaDestino, new BigDecimal(500) );
			assertEquals("1000.8989", cuentaOrigen.getSaldo().toString());
			assertEquals("3000", cuentaDestino.getSaldo().toString());
		}
	}
	
	
	// Con la etiqueta Tag especificamos que pruebas son las que queremos ejecutar, de acuerdo a la configuracion de run configuration.
	@Tag("cuenta")
	@Tag("error")
	@Test
	void testDineroInsuficienteExceptionCuenta() {
		Exception exception = assertThrows(DineroInsuficienteException.class, ()-> {
			cuenta.debito(new BigDecimal(1500));
		});
		String actual = exception.getMessage();
		String esperado = "Dinero Insuficiente";
		assertEquals(esperado, actual);
	}
	
	
	// Con la etiqueta Tag especificamos que pruebas son las que queremos ejecutar, de acuerdo a la configuracion de run configuration.
	@Test
	@Tag("cuenta")
	@Tag("banco")
//	@Disabled   <- con esta anotacion deshabilitamos la prueba
	@DisplayName("Testeando relaciones entre las cuentas y el banco con assertAll.")
	void testRelacionBancoCuentas() {
		Cuenta cuentaDestino = new Cuenta("Jhon Doe", new BigDecimal("2500"));
		Cuenta cuentaOrigen = new Cuenta("Andres", new BigDecimal("1500.8989"));
		
		Banco banco = new Banco();
		banco.addCuenta(cuentaDestino);
		banco.addCuenta(cuentaOrigen);
		
		banco.setNombre("Banco del Estado");
		banco.transferir(cuentaOrigen, cuentaDestino, new BigDecimal(500) );
		
		assertAll(
				() -> assertEquals("1000.8989", cuentaOrigen.getSaldo().toString(), 
						() -> "El valor del saldo de la cuentaOrigen no es el esperado."), 
				() -> assertEquals("3000", cuentaDestino.getSaldo().toPlainString(),
						() -> "El valor del saldo de la cuentaDestino no es el esperado."), 
				() -> assertEquals(2, banco.getCuentas().size(),
						() -> "El banco."), 
				() -> assertEquals("Banco del Estado", cuentaDestino.getBanco().getNombre()), 
				() -> assertEquals("Andres", banco.getCuentas().stream()
							.filter(c -> c.getPersona().equals("Andres"))
							.findFirst()
							.get().getPersona()),
				() -> assertTrue(banco.getCuentas().stream()
							.anyMatch(c -> c.getPersona().equals("Jhon Doe")))
		); //  fin assertAll()
		
	}
	
	
	// Con la anotacion Nested indicamos que vamos a ejecutar una clase anidada
	@Nested
	class SistemaOperativoTest {
		// Este metodo solo se ejecutara en entornos Windows
		@Test
		@EnabledOnOs(OS.WINDOWS)
		void testSoloWindows() {
		}
		
		// Este metodo solo se ejecutara en entornos Linux y Mac
		@Test
		@EnabledOnOs({OS.LINUX, OS.MAC})
		void testSoloLinuxMac() {
		}
		
		// Este metodo NO se ejecutara en entornos Windows
		@Test
		@DisabledOnOs(OS.WINDOWS)
		void testNoWindows() {
		}
	}
	
	
	// Con la anotacion Nested indicamos que vamos a ejecutar una clase anidada
	@Nested
	class JavaVersionTest {
		// Se ejecuta solo con JDK 8
		@Test
		@EnabledOnJre(JRE.JAVA_8)
		void soloJdk8() {
		}
		
		// Se ejecuta solo con JDK 15
		@Test
		@EnabledOnJre(JRE.JAVA_15)
		void SoloJDK15() {
		}
		
		// No se ejecuta si es JDK 15
		@Test
		@DisabledOnJre(JRE.JAVA_15)
		void TestNoJDK15() {
		}
	}
	
	
	// Con la anotacion Nested indicamos que vamos a ejecutar una clase anidada
	@Nested
	class SystemPropertiesTest {
		// Imprime las caracteristicas de sistema
		@Test
		void imprimirSystemProperties() {
			Properties properties = System.getProperties();
			properties.forEach((k,v)-> System.out.println(k + ":" + v));
		}
		
		// Se ejecuta, si la version de Java es especifica a la que pasamos por parametro
		// o si coincide con la expresion regular
		@Test
		@EnabledIfSystemProperty(named = "java.version", matches = ".*17.*")
		void testJavaVersion() {
		}
		
		// se ejecuta solo en arquitectura de 64 bits
		@Test
		@DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*" )
		void testSolo64() {
		}
		
		// se ejectura solo en arquitectura de 32 bits
		@Test
		@EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*" )
		void testNo64() {
		}
		
		// Este test se ejecuta si el nopmbre de usuario es igual a Andres Felipe Lemos
		@Test
		@EnabledIfSystemProperty(named = "user.name", matches = "Andres Felipe Lemos")
		void testUserName() {
		}
		
		@Test
		@EnabledIfSystemProperty(named = "ENV", matches = "dev")
		void testDev() {
		}
	}
	
	
	// Con la anotacion Nested indicamos que vamos a ejecutar una clase anidada
	@Nested
	class VariableAmbienteTest {
		// Este test imprime los datos de configuracion del sistema
		@Test
		void imprimirVariablesAmbiente() {
			Map<String, String> getenv = System.getenv();
			getenv.forEach((k,v)-> System.out.println(k + " = " + v));
		}
		
		// Se jecuta la prueba si en nuestra variable de JAVA_HOME el jdk es 11
		@Test
		@EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-11.0.16.101-hotspot.*")
		void testJavaHome() {
		}
		
		// Se ejecuta la prueba, si poseemos la cantidad de 12 procesadores
		@Test
		@EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches= "12")
		void TestProcesadores() { 
		}
		
		// Esta prueba se ejecuta si confiuramos el IDE como ambiente dev
		@Test
		@EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
		void TestEnv() {
		}
		
		@Test
		@DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod") 
		void TestEnvProdDisabled() {
		}
	}
	
	
	// Con el assumeTrue validamos mediante codigo para ejecutar las pruebas, si el resultado a evaluar no es correcto, no se ejecuta la prueba
	@Test
	void testSaldoCuentaDev() {
		boolean esDev = "dev".equals(System.getProperty("ENV"));
		assumeTrue(esDev);
		assertNotNull(cuenta.getSaldo());
		assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
		assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0); 
		assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); 
	}
	
	
	// Con el assumingThat validamos que bloque de coduigo vamos a ejectura si todo es correcto.
	@Test
	void testSaldoCuentaDev2() {
		boolean esDev = "dev".equals(System.getProperty("ENV"));
		assumingThat(esDev, () -> {
			
			assertNotNull(cuenta.getSaldo());
			assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
			assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0); 
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); 
			
		});
	}
	
	
	/** 
	 * Con la anotacion @RepeatedTest(Numero de veces a repetir) indicamos 
	 * cuantas veces se varepetir el test.
	**/
	@DisplayName("Probando Debito Cuenta Repetir!")
	@RepeatedTest(value=5, name= "{displayName} - Repeticion numero {currentRepetition} de {totalRepetitions}")
	void testDebitoCuenta2(RepetitionInfo info) {
		
		/** Con el parametro RepetitionInfo podemos configurar de manera programatica
		 *  en que iteracion o ciclo ejecute algo puntal que necesitemos, por ejemplo
		 *  aqui se imprimira en consola cuando pase por la iteracion numero 3.
		**/
		if (info.getCurrentRepetition() == 3) {
			System.out.println("Estamos en la repeticion " + info.getCurrentRepetition());
		}
		
		cuenta.debito(new BigDecimal(100));
		assertNotNull(cuenta.getSaldo());
		assertEquals(900, cuenta.getSaldo().intValue());
		assertEquals("900.12345", cuenta.getSaldo().toPlainString());
	}
	
	@Tag("param")
	@Nested
	class PruebasParametrizadasTest {
		
		/** 
		 * Con la anotacion @ParameterizedTest indicamos que los parametros seran parametrizables.
		 * con la anotacion @@ValueSource() indicamos los valores con los que vamos a realizar el test.
		**/
		@ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
		@ValueSource(strings = {"100", "200", "300", "500", "700", "1000.12345"})
		void testDebitoCuentaValueSource(String monto) {
			cuenta.debito(new BigDecimal(monto));
			assertNotNull(cuenta.getSaldo());
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0 );
		}
		
		// medinate la anotacion @CsvSource podemos simular una carga por archivo csv
		@ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
		@CsvSource({"1,100", "2,200", "3,300", "4,500", "5,700", "6,1000.12345"})
		void testDebitoCuentaValueCsvSource(String index, String monto) {
			System.out.println(index + " -> " + monto);
			cuenta.debito(new BigDecimal(monto));
			assertNotNull(cuenta.getSaldo());
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0 );
		}
		
		// con la anotacion @CsvFileSource podemos cargar parametros mediante archivo csv.
		@ParameterizedTest()
		@CsvFileSource(resources = "/data.csv")
		void testDebitoCuentaValueCsvFileSource(String monto) {
			cuenta.debito(new BigDecimal(monto));
			assertNotNull(cuenta.getSaldo());
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0 );
		}
		
		// Simulacion de archivo CSV con varios parametros
		@ParameterizedTest(name = "Ejecutando con valor {0} - {argumentsWithNames}")
		@CsvSource({"200,100,John,Andres", "250,200,Pepe,Pepe", "300,300,maria,Maria", "510,500,Pepa,Pepa", "750,700,Lucas,Luca", "1000.12345,1000.12345,Cata,Cata"})
		void testDebitoCuentaValueCsvSource2(String saldo, String monto, String esperado, String actual) {
			System.out.println(saldo + " -> " + monto);
			cuenta.setSaldo(new BigDecimal(saldo));
			cuenta.debito(new BigDecimal(monto));
			cuenta.setPersona(actual);
			
			assertNotNull(cuenta.getSaldo());
			assertNotNull(cuenta.getPersona());
			assertEquals(esperado, actual);
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0 );
		}
		

		// Ejemplo para test con archivo .csv con varios parametros
		@ParameterizedTest()
		@CsvFileSource(resources = "/data2.csv")
		void testDebitoCuentaValueCsvFileSource2(String saldo, String monto, String esperado, String actual) {
			cuenta.setSaldo(new BigDecimal(saldo));
			cuenta.debito(new BigDecimal(monto));
			cuenta.setPersona(actual);
			
			assertNotNull(cuenta.getSaldo());
			assertNotNull(cuenta.getPersona());
			assertEquals(esperado, actual);
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0 );
		}

	}

	
	// con la anotacion @MethodSource podemos cargar parametros mediante metodos.
	@Tag("param")
	@ParameterizedTest()
	@MethodSource("montoList")
	void testDebitoCuentaValueMethodSource(String monto) {
		cuenta.debito(new BigDecimal(monto));
		assertNotNull(cuenta.getSaldo());
		assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0 );
	}
	
	static private List<String> montoList() {
		return Arrays.asList("100", "200", "300", "500", "700", "1000.12345");
	}
	
	@Nested
	@Tag("timeout")
	class EjemploTimeoutTest {
		
		/**
		 * Con etiqueta @Timeout podemos hacer que el test se rompa si se pasa de x cantidad de tiempo.
		**/
		@Test
		@Timeout(1)
		void pruebaTimeou() throws InterruptedException {
			TimeUnit.SECONDS.sleep(1);
		}
		
		@Test
		@Timeout(value = 1000, unit = TimeUnit.MICROSECONDS)
		void pruebaTimeou2() throws InterruptedException {
			TimeUnit.SECONDS.sleep(800);
		}
		
		@Test
		void testTiemoutAssertions() {
			assertTimeout(Duration.ofSeconds(5), () -> {
				// el TimeUnit.MICROSECONDS.sleep lo que hace es simular una carga pesada o pausa para manejo de timeout.
				TimeUnit.MICROSECONDS.sleep(4000);
			});
		}
	}
	

}
