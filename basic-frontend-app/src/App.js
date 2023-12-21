import {useState, useEffect} from 'react'
import { BrowserRouter as Router, Route } from 'react-router-dom';

import './App.css';
import Header from './components/Header'
import Footer from './components/Footer'
import Tasks from './components/Tasks'
import AddTask from './components/AddTask';
import About from './components/About';

function App() {
  const apiUrl = document.getElementById("api_url").innerHTML
  const [showAddTask, setShowAddTask] = useState(false)
  const [tasks, setTasks] = useState([])

  useEffect(() => {
    const getTasks = async () => {
      const tasksFromServer = await fetchTasks()
      setTasks(tasksFromServer)
    }
    getTasks()
  }, [])  // Ajout de dependency array pour prevenir le 'useEffect' a chaque 'render()'
  // C'est comme le lifecycle event 'ComponentDidMount'

  const fetchTasks = async () => {
    const res = await fetch(apiUrl + '/todos')
    const data = await res.json()
    return data
  }

  const fetchTask = async(id) => {  
    const res = await fetch(apiUrl + `/todos/${id}`)
    const data = await res.json()
    return data
  }

  const addTask = async (task) => {
    const res = await fetch(apiUrl + '/todos',
    {
      method: 'POST',
      headers: {
        'Content-type': 'application/json',
      },
      body: JSON.stringify(task)
    })
    const data = await res.json()
    setTasks([...tasks, data])

    
    // const id = Math.floor(Math.random() * 10000) + 1
    // const newTask = {id, ...task}
    // setTasks([...tasks, newTask])
  }

  const deleteTask = async (id) => {
    await fetch(apiUrl + `/todos/${id}`, {
      method: 'DELETE'
    })
    setTasks(tasks.filter((task) => task.id !== id))
  }

  const toggleReminder = async (id) => {
    const taskToToggle = await fetchTask(id)
    const updTask = await {...taskToToggle, 
      reminder: !taskToToggle.reminder}

    await fetch(apiUrl + `/todos/${id}`,
    {
      method: 'PUT',
      headers: {
        'Content-type': 'application/json'
      },
      body: JSON.stringify(updTask)
    })

    setTasks(
      tasks.map(
        (task) => task.id === id ?
          {...task, reminder: updTask.reminder} : task
      )
    )
  }

  return (
    <Router>
    <div className='container'>
      <Header onAdd={() => setShowAddTask(!showAddTask)}
      showAdd={showAddTask}/>
      
       <Route path='/' exact render={(props) => (
         <>
          {showAddTask && <AddTask onAdd={addTask} />}
          {tasks.length > 0 ?
            <Tasks tasks={tasks} 
              onDelete={deleteTask}
              onToggle={toggleReminder}/>
          : 'No tasks'}   
         </>
       )} />
       <Route path='/about' component={About} />
       <Footer />
    </div>
    </Router>
  );
}

export default App;
