import {useEffect} from 'react'
import AddTask from "./AddTask";

function MyComponent() {
    useEffect(() => {
        alert('Alert 1');
        return () => {
            alert('Alert 2');
        }
    }, [])
    return (
        <div>
            <button onClick={() => {}}> MyButton </button>
        </div>
    );

}
export default AddTask;