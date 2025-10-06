import { taskApi } from "@/api/taskApi";
import { UseAppContext } from "@/ContextProvider";
import React from "react";
import '../styles/form.scss';

export function Formulario({
  setTarea,
}: {
  setTarea: React.Dispatch<React.SetStateAction<Tarea[]>>;
}) {
  const { jwt, refresh } = UseAppContext();
  const [texttarea, setTextTarea] = React.useState("");
  const [message, setMessage] = React.useState('');
  const submit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      const newtarea = await taskApi.save(jwt, { title: texttarea });
      setTarea((d) => [newtarea, ...d]);
      setTextTarea("");
      setMessage('');
    } catch (error) {      if (error == "refresh") {
        const newjet = await refresh();
        const newtarea = await taskApi.save(newjet, { title: texttarea });
        setTarea((d) => [newtarea, ...d]);
        setTextTarea("");
        setMessage('')
      }else{
        setMessage((error as Error).message);
      }
    }
  };
  return (
    <div className="formulario-container">
      <form onSubmit={submit} id="tareaform">
        <input
          type="text"
          placeholder="Nueva tarea"
          value={texttarea}
          onChange={(e) => setTextTarea(e.target.value)}
        />
        <button type="submit">Agregar</button>
        {message.trim() && <p className="error-message">{message}</p>}
      </form>
    </div>
  );
}
