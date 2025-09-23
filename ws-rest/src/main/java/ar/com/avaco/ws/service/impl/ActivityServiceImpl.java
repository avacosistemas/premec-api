package ar.com.avaco.ws.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.arc.sec.repository.UsuarioRepository;
import ar.com.avaco.ws.dto.actividad.HorasPorEmpleadoDTO;
import ar.com.avaco.ws.dto.actividad.RegistroPreviewEmpleadoMensualDTO;
import ar.com.avaco.ws.dto.employee.liquidacion.FueraConvenio;
import ar.com.avaco.ws.dto.employee.liquidacion.Jornal;
import ar.com.avaco.ws.dto.employee.liquidacion.Mensual;
import ar.com.avaco.ws.dto.employee.liquidacion.PlantillaData;
import ar.com.avaco.ws.service.AbstractSapService;

@Service("activityService")
public class ActivityServiceImpl extends AbstractSapService implements ActivityService {

	@Autowired
	private SQLServerConnection sqlcon;

	private UsuarioRepository usuarioRepository;

	@Override
	public List<RegistroPreviewEmpleadoMensualDTO> obtenerActividadesValoradas(String fechaDesde, String fechaHasta,
			String exclusiones) {

		List<Usuario> usuarios = usuarioRepository.findAll();

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ")
			   .append("    cab.UserID AS usuarioSap, ")
			   .append("    E.firstName + ' ' + E.lastName AS nombre, ")
			   .append("    SUM(det.BillableHr) AS facturables, ")
			   .append("    dbo.fnFormatHoras(SUM(det.BillableHr)) AS facturablesHora, ")
			   .append("    SUM(det.NonBillHr) AS ociosas, ")
			   .append("    dbo.fnFormatHoras(SUM(det.NonBillHr)) AS ociosasHora, ")
			   .append("    SUM(CASE WHEN det.u_tipoausentismo IS NULL OR det.u_tipoausentismo = '' ")
			   .append("        THEN det.EffectHr ELSE 0 END) AS fichado, ")
			   .append("    dbo.fnFormatHoras(SUM(CASE WHEN det.u_tipoausentismo IS NULL OR det.u_tipoausentismo = '' ")
			   .append("        THEN det.EffectHr ELSE 0 END)) AS fichadoHora, ")
			   .append("    CAST(CAST((SUM(det.BillableHr) * 100.0) / NULLIF(SUM(CASE WHEN det.u_tipoausentismo IS NULL OR det.u_tipoausentismo = '' ")
			   .append("        THEN det.EffectHr ELSE 0 END), 0) AS DECIMAL(10,2)) AS VARCHAR(10)) + '%' AS efectividad, ")
			   .append("    ISNULL(O.cantMB,0) AS cantMB, ")
			   .append("    ISNULL(O.cantB,0) AS cantB, ")
			   .append("    ISNULL(O.cantR,0) AS cantR, ")
			   .append("    ISNULL(O.cantM,0) AS cantM, ")
			   .append("    ISNULL(O.cantSV,0) AS cantSV, ")
			   .append("    ISNULL(O.cantidadActividades,0) AS cantidadActividades, ")
			   .append("    FORMAT(CAST(NULLIF(ISNULL(O.cantMB,0) + ISNULL(O.cantB,0),0) AS DECIMAL(10,2)) / ")
			   .append("        CAST(((ISNULL(O.cantMB,0) * 1) + (ISNULL(O.cantB,0) * 1) + (ISNULL(O.cantR,0) * 5) + (ISNULL(O.cantM,0) * 15)) AS DECIMAL(10,2)), 'P2') AS porcentajeValoracion, ")
			   .append("    E.U_Objetivo AS objetivoActividades, ")
			   .append("    CAST(CAST((O.cantidadActividades * 100.0) / NULLIF(E.U_Objetivo,0) AS DECIMAL(10,2)) AS VARCHAR(10)) + '%' AS cumplimientoObjetivo, ")
			   .append("    E.salary AS salario, ")
			   .append("    E.salaryunit AS unidadSalario ")
			   .append(" FROM OTSH cab ")
			   .append(" LEFT JOIN TSH1 det ON cab.AbsEntry = det.AbsEntry ")
			   .append(" INNER JOIN OHEM E ON cab.UserID = E.empID ")
			   .append(" LEFT JOIN ( ")
			   .append("    SELECT ")
			   .append("        AttendEmpl, ")
			   .append("        COUNT(*) AS cantidadActividades, ")
			   .append("        SUM(CASE WHEN U_Valoracion = 'MB' THEN 1 ELSE 0 END) AS cantMB, ")
			   .append("        SUM(CASE WHEN U_Valoracion = 'B' THEN 1 ELSE 0 END) AS cantB, ")
			   .append("        SUM(CASE WHEN U_Valoracion = 'R' THEN 1 ELSE 0 END) AS cantR, ")
			   .append("        SUM(CASE WHEN U_Valoracion = 'M' THEN 1 ELSE 0 END) AS cantM, ")
			   .append("        SUM(CASE WHEN U_Valoracion IS NULL THEN 1 ELSE 0 END) AS cantSV ")
			   .append("    FROM OCLG ")
			   .append("    WHERE Recontact BETWEEN '{fechaDesde}' AND '{fechaHasta}' ");
		   
		   
			List<String> exclusionesList = Lists.newArrayList(exclusiones.split(","));
			for (String exclusion : exclusionesList) {
				sql.append(" and Details not like '%{exclusion}%' ".replace("{exclusion}", exclusion));
			}
		
		   
		   sql.append("    GROUP BY AttendEmpl ")
			   .append(") O ON O.AttendEmpl = E.empID ")
			   .append("WHERE cab.DateFrom = '{fechaDesde}' ");
		   
		   
		   sql.append(" GROUP BY ")
			   .append("    cab.DateFrom, ")
			   .append("    cab.UserID, ")
			   .append("    E.firstName, ")
			   .append("    E.lastName, ")
			   .append("    E.U_Objetivo, ")
			   .append("    E.salary, ")
			   .append("    E.salaryunit, ")
			   .append("    O.cantidadActividades, ")
			   .append("    O.cantMB, ")
			   .append("    O.cantB, ")
			   .append("    O.cantR, ")
			   .append("    O.cantM, ")
			   .append("    O.cantSV ")
			   .append("ORDER BY E.firstName, E.lastName;");
		   
		String sqlString = sql.toString().replace("{fechaDesde}", fechaDesde).replace("{fechaHasta}", fechaHasta);
		
		List<RegistroPreviewEmpleadoMensualDTO> lista = new ArrayList<RegistroPreviewEmpleadoMensualDTO>();

		try (Connection conn = sqlcon.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sqlString);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				RegistroPreviewEmpleadoMensualDTO preview = new RegistroPreviewEmpleadoMensualDTO();
				preview.setUsuarioSap(rs.getLong("usuarioSap"));
				preview.setNombre(rs.getString("nombre"));

				BigDecimal facturables = rs.getBigDecimal("facturables");
				preview.setFacturables(facturables != null ? facturables.toString() : "");
				preview.setFacturablesHora(rs.getString("facturablesHora"));

				BigDecimal ociosas = rs.getBigDecimal("ociosas");
				preview.setOciosas(ociosas != null ? ociosas.toString() : "");
				preview.setOciosasHora(rs.getString("ociosasHora"));

				BigDecimal fichado = rs.getBigDecimal("fichado");
				preview.setFichado(fichado != null ? fichado.toString() : "");
				preview.setFichadoHora(rs.getString("fichadoHora"));

				preview.setEfectividad(rs.getString("efectividad"));

				preview.setCantMB(rs.getInt("cantMB"));
				preview.setCantB(rs.getInt("cantB"));
				preview.setCantR(rs.getInt("cantR"));
				preview.setCantM(rs.getInt("cantM"));
				preview.setCantSV(rs.getInt("cantSV"));

				preview.setCantidadActividades(rs.getInt("cantidadActividades"));
				preview.setPorcentajeValoracion(rs.getString("porcentajeValoracion"));

				preview.setObjetivoActividades(rs.getInt("objetivoActividades"));
				preview.setCumplimientoObjetivo(rs.getString("cumplimientoObjetivo"));

				preview.setSalario(rs.getBigDecimal("salario").toString());
				preview.setUnidadSalario(rs.getString("unidadSalario"));

				Optional<Usuario> usuario = usuarios.stream()
						.filter(x -> x.getUsuariosap().equals(preview.getUsuarioSap().toString())).findFirst();
				if (usuario.isPresent()) {
					preview.setLegajo(usuario.get().getLegajo());
				} else {
					// Error
					preview.setLegajo(-1);
				}

				lista.add(preview);
			}

			sqlcon.getConnection().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return lista;
	}

	@Override
	public List<HorasPorEmpleadoDTO> obtenerHorasAgrupadasPorFechaEmpleado(List<Long> employeeIds, String fechaDesde,
			String fechaHasta, String exclusionesActividadesCalculoHorasNetas) {
		return obtenerHorasAgrupadasPorFechaEmpleado(employeeIds, fechaDesde, fechaHasta, null, null,
				exclusionesActividadesCalculoHorasNetas);
	}

	@Override
	public List<HorasPorEmpleadoDTO> obtenerHorasAgrupadasPorFechaEmpleado(Long employeeId, String fechaDesde,
			String fechaHasta, String horaDesde, String horaHasta, String exclusionesActividadesCalculoHorasNetas) {
		return obtenerHorasAgrupadasPorFechaEmpleado(Collections.singletonList(employeeId), fechaDesde, fechaHasta,
				null, null, exclusionesActividadesCalculoHorasNetas);
	}

	private List<HorasPorEmpleadoDTO> obtenerHorasAgrupadasPorFechaEmpleado(List<Long> employeeIds, String fechaDesde,
			String fechaHasta, String horaDesde, String horaHasta, String exclusionesActividadesCalculoHorasNetas) {

		List<String> ids = new ArrayList<>();
		employeeIds.forEach(x -> ids.add(x.toString()));

		String sql = " SELECT SUM(act.duration) AS duration, act.recontact, act.AttendEmpl " + " FROM OCLG act "
				+ " LEFT JOIN OHEM emp ON act.AttendEmpl = emp.empID " + " WHERE 1 = 1 "
				+ " AND act.AttendEmpl IN ({ids}) ".replace("{ids}", String.join(",", ids))
				+ " AND act.recontact >= '{fechaDesde}' ".replace("{fechaDesde}", fechaDesde)
				+ " AND act.recontact <= '{fechaHasta}' ".replace("{fechaHasta}", fechaHasta);

		if (horaDesde != null && horaHasta != null) {
			String[] splitDesde = horaDesde.split(":");
			String desdeInt = new Integer(splitDesde[0] + splitDesde[1]).toString();
			String[] splitHasta = horaHasta.split(":");
			String hastaInt = new Integer(splitHasta[0] + splitHasta[1]).toString();
			sql += " AND act.BeginTime >= '{horaDesde}' ".replace("{horaDesde}", desdeInt);
			sql += " AND act.BeginTime <= '{horaHasta}' ".replace("{horaHasta}", hastaInt);
		}

		List<String> exclusiones = Lists.newArrayList(exclusionesActividadesCalculoHorasNetas.split(","));
		for (String exclusion : exclusiones) {
			sql += " and act.Details not like '%{exclusion}%' ".replace("{exclusion}", exclusion);
		}

		sql += " group by act.recontact, act.attendempl ";

		List<HorasPorEmpleadoDTO> lista = new ArrayList<HorasPorEmpleadoDTO>();

		try (Connection conn = sqlcon.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				int duration = rs.getInt("duration");
				Date seStartDat = rs.getDate("recontact");
				int attendEmpl = rs.getInt("AttendEmpl");

				HorasPorEmpleadoDTO resumen = new HorasPorEmpleadoDTO(attendEmpl, seStartDat, duration);
				lista.add(resumen);
			}

			sqlcon.getConnection().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return lista;

	}

	@Override
	public PlantillaData getPreviewNovedadesContador(String anio, String mes) {

		String periodo = anio + StringUtils.leftPad(mes, 2, "0") + "01";

		String sql = "SELECT " + 
				"	LTRIM(RTRIM(CAST(CAB.UserID AS VARCHAR))) AS UserID, " + 
				"	LTRIM(RTRIM(CAST(CAB.LastName AS VARCHAR))) AS Apellido, " + 
				"	LTRIM(RTRIM(CAST(CAB.FirstName AS VARCHAR))) AS Nombre, " +
				"   LTRIM(RTRIM(CAST(CAB.u_aumento AS VARCHAR))) AS Gratificaciones, " +
				"	LTRIM(RTRIM(CAST(E.u_cuil AS VARCHAR))) AS Cuil, " + 
				"	LTRIM(RTRIM(CAST(E.u_categoria AS VARCHAR))) AS Categoria, " + 
				"	LTRIM(RTRIM(CAST(FORMAT(E.salary, 'N2', 'es-ES') as varchar))) AS Salary, " + 
				"	E.salaryunit AS SalaryUnit, " + 
				"	LTRIM(RTRIM(REPLACE(STR(ISNULL(H.Total_hsnormales, 0), 10, 1), '.0', ''))) AS hsNormales, " + 
				"	LTRIM(RTRIM(REPLACE(STR(ISNULL(H.Total_hsferiado, 0), 10, 1), '.0', ''))) AS hsFeriado, " + 
				"	LTRIM(RTRIM(CAST(CAB.U_adelanto AS VARCHAR))) AS Adelanto, " + 
				"	LTRIM(RTRIM(CAST(CAB.U_prestamo AS VARCHAR))) AS Prestamo, " + 
				"	LTRIM(RTRIM(REPLACE(STR(ISNULL(H.Total_hsextras50, 0), 10, 1), '.0', ''))) AS Total_hsextras50, " + 
				"	LTRIM(RTRIM(REPLACE(STR(ISNULL(H.Total_hsextras100, 0), 10, 1), '.0', ''))) AS Total_hsextras100, " + 
				"	LTRIM(RTRIM(REPLACE(STR(ISNULL(H.Total_hsextrasferiado, 0), 10, 1), '.0', ''))) AS Total_hsextrasFeriado, " + 
				"	LTRIM(RTRIM(CAST(CAB.U_premio AS VARCHAR))) AS Premio, " + 
				"	LTRIM(RTRIM(CAST(ISNULL(H.Total_comidas, 0) AS VARCHAR))) AS comida, " + 
				"	ISNULL(Aus.Ausencias, '') AS Ausencias " + 
				"FROM " + 
				"	OTSH CAB " + 
				"INNER JOIN OHEM E  " + 
				"	ON CAB.UserID = E.empID " + 
				"LEFT JOIN ( " + 
				"	SELECT " + 
				"		AbsEntry, " + 
				"		SUM(dbo.fnHorasDecimal(U_hsnormales)) AS Total_hsnormales, " + 
				"		SUM(dbo.fnHorasDecimal(U_hsextras50)) AS Total_hsextras50, " + 
				"		SUM(dbo.fnHorasDecimal(U_hsextras100)) AS Total_hsextras100, " + 
				"		SUM(dbo.fnHorasDecimal(U_hsferiado)) AS Total_hsferiado, " + 
				"		SUM(dbo.fnHorasDecimal(U_hsextrasferiado)) AS Total_hsextrasferiado, " + 
				"		SUM(CASE WHEN U_comidas = 'SI' THEN 1 ELSE 0 END) AS Total_comidas " + 
				"	FROM " + 
				"		TSH1 " + 
				"	GROUP BY " + 
				"		AbsEntry " + 
				") H  " + 
				"	ON CAB.AbsEntry = H.AbsEntry " + 
				"LEFT JOIN ( " + 
				"	SELECT DISTINCT t1.AbsEntry, " + 
				"		STUFF(( " + 
				"			SELECT ', ' + CONVERT(varchar(10), t2.[Date], 103) + ' ' + t2.U_tipoausentismo " + 
				"			FROM TSH1 t2 " + 
				"			WHERE t1.AbsEntry = t2.AbsEntry " + 
				"			FOR XML PATH(''), TYPE " + 
				"		).value('.', 'NVARCHAR(MAX)'), 1, 2, '') AS Ausencias " + 
				"	FROM TSH1 t1 " + 
				") Aus  " + 
				"	ON CAB.AbsEntry = Aus.AbsEntry "
				+ " WHERE CAB.DateFrom = '{periodo}';".replace("{periodo}", periodo);

		List<Jornal> jornalesList = new ArrayList<>();
		List<Mensual> mensualesList = new ArrayList<>();
		List<FueraConvenio> fueraConvenioList = new ArrayList<>();

		try (Connection conn = sqlcon.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {

				String tipoSalario = rs.getString("salaryunit");

				switch (tipoSalario) {
				case "H":
					Jornal j = generarJornal(rs);
					jornalesList.add(j);
					break;
				case "M":
					Mensual m = generarMensual(rs);
					mensualesList.add(m);
					break;
				case "Y":
					FueraConvenio fc = generarFueraConvenio(rs);
					fueraConvenioList.add(fc);
					break;
				default:
					break;
				}
			}

			sqlcon.getConnection().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		PlantillaData pd = new PlantillaData();
		pd.getFueraConvenio().addAll(fueraConvenioList);
		pd.getJornales().addAll(jornalesList);
		pd.getMensual().addAll(mensualesList);
		pd.setPeriodo(anio + "-" + mes);

		return pd;

	}

	private FueraConvenio generarFueraConvenio(ResultSet rs) throws SQLException {

		FueraConvenio m = new FueraConvenio();
		m.setLegajo(rs.getString("UserID"));
		m.setApellido(rs.getString("Apellido"));
		m.setNombre(rs.getString("Nombre"));
		m.setCuil(rs.getString("Cuil"));
		m.setCategoria(rs.getString("Categoria"));

		String feriado = rs.getString("hsFeriado");
		if (StringUtils.isNotBlank(feriado)) {
			Long f = Long.parseLong(feriado);
			Long dias = f / 8L;
			feriado = dias.toString();
		}
		// Calcular en base a novedades
		m.setFeriado(feriado);

		m.setAdelantoSueldo(rs.getString("Adelanto"));
		m.setPrestamos(rs.getString("Prestamo"));
		m.setAusencias(rs.getString("Ausencias"));
		m.setGratificaciones(rs.getString("Gratificaciones"));
		
		return m;
	}

	private Mensual generarMensual(ResultSet rs) throws SQLException {
		Mensual m = new Mensual();
		m.setLegajo(rs.getString("UserID"));
		m.setApellido(rs.getString("Apellido"));
		m.setNombre(rs.getString("Nombre"));
		m.setCuil(rs.getString("Cuil"));
		m.setCategoria(rs.getString("Categoria"));

		m.setValorHora(rs.getString("Salary"));

		String feriado = rs.getString("hsFeriado");
		if (StringUtils.isNotBlank(feriado)) {
			Long f = Long.parseLong(feriado);
			Long dias = f / 8L;
			feriado = dias.toString();
		}
		
		m.setFeriado(feriado);

		m.setHsNormales(rs.getString("hsNormales"));

		m.setFeriadoExtra(rs.getString("total_hsextrasFeriado"));

		m.setAdelantoSueldo(rs.getString("Adelanto"));
		m.setPrestamos(rs.getString("Prestamo"));
		m.setHs50(rs.getString("Total_hsextras50"));
		m.setHs100(rs.getString("Total_hsextras100"));
		m.setPremio(rs.getString("Premio"));
		m.setComida(rs.getString("Comida"));
		m.setAusencias(rs.getString("Ausencias"));
		m.setGratificaciones(rs.getString("Gratificaciones"));
		
		return m;
	}

	private Jornal generarJornal(ResultSet rs) throws SQLException {
		Jornal m = new Jornal();
		m.setLegajo("SAP " + rs.getString("UserID"));
		m.setApellido(rs.getString("Apellido"));
		m.setNombre(rs.getString("Nombre"));
		m.setCuil(rs.getString("Cuil"));
		m.setCategoria(rs.getString("Categoria"));

		m.setValorHora(rs.getString("Salary"));

		m.setFeriado(rs.getString("hsFeriado"));

		m.setHsNormales(rs.getString("hsNormales"));

		m.setFeriadoExtra(rs.getString("total_hsextrasFeriado"));

		m.setAdelantoSueldo(rs.getString("Adelanto"));
		m.setPrestamos(rs.getString("Prestamo"));
		m.setHs50(rs.getString("Total_hsextras50"));
		m.setHs100(rs.getString("Total_hsextras100"));
		m.setPremio(rs.getString("Premio"));
		m.setComida(rs.getString("Comida"));
		m.setAusencias(rs.getString("Ausencias"));
		m.setGratificaciones(rs.getString("Gratificaciones"));
		return m;
	}

	@Resource(name = "usuarioRepository")
	public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

}
