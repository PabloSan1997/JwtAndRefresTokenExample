import { convertTime } from "@/utils/convertTime";
import "../styles/tarea.scss";
interface ITareaCom extends Tarea {
  deleteId(id: number): void;
  update(id: number, data: TareaDto): void;
  isprocessing: boolean;
  setIsprocessing(a: boolean): void;
}

export function TareaCom({
  id,
  updatedAt,
  createdAt,
  title,
  state,
  deleteId,
  update,
  isprocessing,
  setIsprocessing,
}: ITareaCom) {
  return (
    <div className={state?"tarea active":"tarea"}>
      <button
        className="delete-button"
        onClick={() => {
          setIsprocessing(true);
          deleteId(id);
        }}
        disabled={isprocessing}
      >
        X
      </button>
      <p>{title}</p>
      <span>Creada: {convertTime(createdAt)}</span>
      {state && <span>Realizada: {convertTime(updatedAt)}</span>}
      <button
        className="complete-button"
        onClick={() => {
          setIsprocessing(true);
          update(id, { title, state: !state });
        }}
        disabled={isprocessing}
      >
        ✔
      </button>
    </div>
  );
}
