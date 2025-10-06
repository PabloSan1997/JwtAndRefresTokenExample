import { Formulario } from "@/components/Formulario";
import { TareaCom } from "@/components/TareaCom";
import { UseAppContext } from "@/ContextProvider";
import { useMyTasks } from "@/hook/taskHook";
import { createFileRoute, Navigate } from "@tanstack/react-router";
import '../styles/mytasks.scss'

export const Route = createFileRoute("/mytasks")({
  component: RouteComponent,
});

function RouteComponent() {
  const { jwt } = UseAppContext();
  const {
    setTarea,
    tarea,
    deleteId,
    updateById,
    isprocessing,
    setIsprocessing,
  } = useMyTasks();
  if (!jwt.trim()) return <Navigate to="/login" />;
  return (
    <div className="mytasks-container">
      <Formulario setTarea={setTarea} />
      <div className="tarea-container">
        {tarea.map((p) => (
          <TareaCom
            key={p.id}
            {...p}
            deleteId={deleteId}
            update={updateById}
            isprocessing={isprocessing}
            setIsprocessing={setIsprocessing}
          />
        ))}
      </div>
    </div>
  );
}
