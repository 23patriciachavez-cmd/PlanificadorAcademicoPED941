package sv.edu.udb.ped941.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdministradorController {

    private final PlanificadorWebService servicio;

    public AdministradorController(
            PlanificadorWebService servicio) {

        this.servicio = servicio;
    }

    /// DASHBOARD ADMINISTRATIVO
    
    @GetMapping
    public String panel(Model model) {

        model.addAttribute(
                "materias",
                servicio.listarMaterias());

        model.addAttribute(
                "totalMaterias",
                servicio.totalMaterias());

        model.addAttribute(
                "totalUV",
                servicio.totalUV());

        model.addAttribute(
                "pensumValido",
                servicio.pensumValido());

        model.addAttribute(
                "grafoSinCiclos",
                servicio.grafoSinCiclos());

        model.addAttribute(
                "electivas",
                servicio.listarElectivas());

        model.addAttribute(
                "grafo",
                servicio.mostrarGrafo());

        model.addAttribute(
                "pensumJerarquico",
                servicio.mostrarPensumJerarquico());

        model.addAttribute(
                "orden",
                servicio.generarOrdenAcademico());

        return "admin";
    }

    /// REGISTRAR MATERIA
    
    @PostMapping("/registrar")
    public String registrar(

            @RequestParam("codigo") String codigo,

            @RequestParam("nombre") String nombre,

            @RequestParam("uv") int uv,

            @RequestParam("ciclo") int ciclo,

            @RequestParam("area") String area,

            @RequestParam(
                    name = "obligatoria",
                    defaultValue = "false")
            boolean obligatoria,

            RedirectAttributes redirect) {

        return ejecutar(
                () -> servicio.registrarMateria(
                        codigo,
                        nombre,
                        uv,
                        ciclo,
                        area,
                        obligatoria),

                "Materia registrada correctamente.",

                redirect);
    }

    /// MODIFICAR MATERIA
    
    @PostMapping("/modificar")
    public String modificar(

            @RequestParam("codigo") String codigo,

            @RequestParam("nombre") String nombre,

            @RequestParam("uv") int uv,

            @RequestParam("ciclo") int ciclo,

            @RequestParam("area") String area,

            @RequestParam(
                    name = "obligatoria",
                    defaultValue = "false")
            boolean obligatoria,

            RedirectAttributes redirect) {

        return ejecutar(
                () -> servicio.modificarMateria(
                        codigo,
                        nombre,
                        uv,
                        ciclo,
                        area,
                        obligatoria),

                "Materia modificada correctamente.",

                redirect);
    }

    /// AGREGAR PRERREQUISITO
    
    @PostMapping("/agregar-prerrequisito")
    public String agregarPrerequisito(

            @RequestParam("origen") String origen,

            @RequestParam("destino") String destino,

            RedirectAttributes redirect) {

        return ejecutar(
                () -> servicio.agregarPrerequisito(
                        origen,
                        destino),

                "Prerrequisito agregado correctamente.",

                redirect);
    }

    /// ELIMINAR PRERREQUISITO
    
    @PostMapping("/eliminar-prerrequisito")
    public String eliminarPrerequisito(

            @RequestParam("origen") String origen,

            @RequestParam("destino") String destino,

            RedirectAttributes redirect) {

        return ejecutar(
                () -> servicio.eliminarPrerequisito(
                        origen,
                        destino),

                "Prerrequisito eliminado correctamente.",

                redirect);
    }

    /// CONTROL DE ERRORES
    
    private String ejecutar(
            Runnable operacion,
            String mensaje,
            RedirectAttributes redirect) {

        try {

            operacion.run();

            redirect.addFlashAttribute(
                    "mensaje",
                    mensaje);

        } catch (IllegalArgumentException ex) {

            redirect.addFlashAttribute(
                    "error",
                    ex.getMessage());
        }

        return "redirect:/admin";
    }
}
