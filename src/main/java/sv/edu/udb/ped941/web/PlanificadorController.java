package sv.edu.udb.ped941.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sv.edu.udb.ped941.modelo.Materia;

@Controller
public class PlanificadorController {

    private final PlanificadorWebService servicio;

    public PlanificadorController(
            PlanificadorWebService servicio) {

        this.servicio = servicio;
    }

    @GetMapping("/")
    public String inicio(
            @RequestParam(
                    name = "codigo",
                    required = false)
            String codigo,
            Model model) {

        cargarDashboard(model);

        if (codigo != null && !codigo.isBlank()) {

            Materia materia =
                    servicio.buscarMateria(codigo);

            if (materia == null) {

                model.addAttribute(
                        "consultaError",
                        "La materia indicada no existe.");

            } else {

                String codigoReal =
                        materia.getCodigo();

                model.addAttribute(
                        "materiaConsulta",
                        materia);

                model.addAttribute(
                        "prerrequisitosConsulta",
                        servicio.prerrequisitosDe(codigoReal));

                model.addAttribute(
                        "dependientesConsulta",
                        servicio.materiasDependientesDe(codigoReal));

                model.addAttribute(
                        "cadenaConsulta",
                        servicio.cadenaCompletaPrerequisitos(codigoReal));
            }
        }

        return "index";
    }

    @PostMapping("/materias/{codigo}/estado")
    public String cambiarEstado(
            @PathVariable("codigo") String codigo,
            RedirectAttributes redirect) {

        try {

            servicio.alternarMateria(codigo);

            redirect.addFlashAttribute(
                    "mensaje",
                    "Estado de la materia actualizado.");

        } catch (IllegalArgumentException ex) {

            redirect.addFlashAttribute(
                    "error",
                    ex.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/electiva/seleccionar")
    public String seleccionarElectiva(
            @RequestParam("codigo") String codigo,
            RedirectAttributes redirect) {

        try {

            servicio.seleccionarElectiva(codigo);

            redirect.addFlashAttribute(
                    "mensaje",
                    "Electiva guardada correctamente.");

        } catch (IllegalArgumentException ex) {

            redirect.addFlashAttribute(
                    "error",
                    ex.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/electiva/limpiar")
    public String limpiarElectiva(
            RedirectAttributes redirect) {

        try {

            servicio.limpiarElectiva();

            redirect.addFlashAttribute(
                    "mensaje",
                    "Seleccion de electiva eliminada.");

        } catch (IllegalArgumentException ex) {

            redirect.addFlashAttribute(
                    "error",
                    ex.getMessage());
        }

        return "redirect:/";
    }

    private void cargarDashboard(Model model) {

        List<Materia> simulacion =
                servicio.simularProximoCiclo();

        model.addAttribute(
                "materias",
                servicio.listarMaterias());

        model.addAttribute(
                "disponibles",
                servicio.materiasDisponibles());

        model.addAttribute(
                "bloqueadas",
                servicio.materiasBloqueadas());

        model.addAttribute(
                "proximoCiclo",
                simulacion);

        model.addAttribute(
                "cantidadProximoCiclo",
                simulacion.size());

        model.addAttribute(
                "electivas",
                servicio.listarElectivas());

        model.addAttribute(
                "totalMaterias",
                servicio.totalMaterias());

        model.addAttribute(
                "totalUV",
                servicio.totalUV());

        model.addAttribute(
                "materiasAprobadas",
                servicio.materiasAprobadas());

        model.addAttribute(
                "uvAprobadas",
                servicio.uvAprobadas());

        model.addAttribute(
                "porcentajeMaterias",
                servicio.porcentajeMaterias());

        model.addAttribute(
                "porcentajeUV",
                servicio.porcentajeUV());

        model.addAttribute(
                "siguienteCiclo",
                servicio.siguienteCiclo());

        model.addAttribute(
                "pensumValido",
                servicio.pensumValido());

        model.addAttribute(
                "electivaSeleccionada",
                servicio.electivaSeleccionada());

        model.addAttribute(
                "electivaHabilitada",
                servicio.puedeCursarElectiva());
    }
}
