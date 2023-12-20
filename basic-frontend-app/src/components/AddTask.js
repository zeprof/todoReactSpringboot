import {useState} from 'react'

const AddTask = ({onAdd}) => {
    const [description, setDescription] = useState('')
    const [zedate, setZedate] = useState('')
    const [reminder, setReminder] = useState(false)

    const onSubmit = (e) => {
        e.preventDefault()

        if (!description) {
            alert('Please add task')
            return
        }

        onAdd({description, zedate, reminder})
        setDescription('')
        setZedate('')
        setReminder(false)
    }

    return (
        <form className='add-form' onSubmit={onSubmit}>
            <div className='form-control'>
                <label>Task</label>
                <input type='text' placeholder='AddTask'
                value={description}
                onChange={(e) => setDescription(e.target.value)} />
            </div>
            <div className='form-control'>
                <label>Date and Time</label>
                <input type='text' placeholder='Date and Time'
                value={zedate}
                onChange={(e) => setZedate(e.target.value)} />
            </div>
            <div className='form-control form-control-check'>
                <label>Set Reminder</label>
                <input type='checkbox'
                    value={reminder}
                    checked={reminder} 
                    onChange={(e) => setReminder(e.currentTarget.checked)} />
            </div>
            <input type='submit' value='Save Task' className='btn btn-block'/>
        </form>
    )
}

export default AddTask
