import { taskApi } from "@/api/taskApi";
import { UseAppContext } from "@/ContextProvider";
import React from "react";

export const useMyTasks = () => {
  const { jwt, refresh } = UseAppContext();
  const [tarea, setTarea] = React.useState<Tarea[]>([]);
  const [isprocessing, setIsprocessing] = React.useState(false);
  const deleteId = async (id: number) => {
    try {
      const iddelete = await taskApi.deleteById(jwt, id);
      setTarea((t) => t.filter((f) => f.id != iddelete.id));
      setIsprocessing(false);
    } catch (error) {
      if (error == "refresh") {
        const jwtnew = await refresh();
        const iddelete = await taskApi.deleteById(jwtnew, id);
        setTarea((t) => t.filter((f) => f.id != iddelete.id));
        setIsprocessing(false);
      }
    }
  };
  const updateById = async (id: number, tarea: TareaDto) => {
    try {
      const tar = await taskApi.updateById(jwt, id, tarea);
      setTarea((t) => {
        const c = [...t];
        const i = c.findIndex((p) => p.id == id);
        c[i] = {
          ...tar,
        };
        return c;
      });
      setIsprocessing(false);
    } catch (error) {
      if (error == "refresh") {
        const jwtnew = await refresh();
        const iddelete = await taskApi.updateById(jwtnew, id, tarea);
        setTarea((t) => t.filter((f) => f.id != iddelete.id));
      }
      setIsprocessing(false);
    }
  };

  React.useEffect(() => {
    if (jwt.trim() && jwt != "init")
      taskApi
        .findAll(jwt)
        .then(setTarea)
        .catch((err) => {
          if ((err as "refresh") == "refresh") refresh();
        });
  }, [jwt]);

  return {
    tarea,
    deleteId,
    updateById,
    setTarea,
    isprocessing,
    setIsprocessing,
  };
};
